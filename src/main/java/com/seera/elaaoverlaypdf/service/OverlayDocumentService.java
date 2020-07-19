package com.seera.elaaoverlaypdf.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * 
 * @author ricardopaulo
 *
 */

@Service
@Path("/")
@PropertySource("classpath:application.properties")
public class OverlayDocumentService implements OverlayDocumentServiceInterface {
	
	@Value( "${overlay.x}" )
	private int xPos;
	
	@Value( "${overlay.y}" )
	private int yPos;
	
	@Value( "${overlay.height}" )
	private int height;
	
	@Value( "${pdf.img.dpi}" )
	private int dpi;
	
	
	
	@Autowired
	private ImageHelperService imageHelperService;
	
	
	@GET
	@Path("/management/health")
	public Response healthCheck() {
		return Response.status(200).entity("I am as great as I could be!").build();
	}

	@POST
	@Path("/etoQrCode.pdf")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("eto") InputStream etoDoc, @FormDataParam("qrcode") InputStream qrCodeDoc) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Response response = null;
		try {
			PDDocument eto = PDDocument.load(etoDoc);
			PDFRenderer qrCode = new PDFRenderer(PDDocument.load(qrCodeDoc));

			if (eto.getPages().getCount() > 0) {
				PDPage firstPage = eto.getPage(0);
				PDPageContentStream contentStream = new PDPageContentStream(eto, firstPage, AppendMode.APPEND, true, true);
				BufferedImage stampBufferedImg = qrCode.renderImageWithDPI(0, dpi);
				stampBufferedImg = imageHelperService.grayScale(stampBufferedImg);
				final byte[] imgByte = imageHelperService.bufferedImgToByte(stampBufferedImg);
				PDImageXObject stampXObject = PDImageXObject.createFromByteArray(eto, imgByte, "stamp");
				final float wRatioPic =   ((float) stampBufferedImg.getWidth()) / ((float) stampBufferedImg.getHeight());
				final float width = ((float) height) * wRatioPic;
				contentStream.drawImage(stampXObject, xPos, yPos, (int) width, height);
				contentStream.close();
				eto.save(outputStream);
				
				StreamingOutput fileContent = (output) -> output.write(outputStream.toByteArray());
				response = Response.status(Status.CREATED).entity(fileContent).build();
			} else {
				response = Response.status(Status.BAD_REQUEST).entity("Both files need to have at least 1 page.").build();
			}
			eto.close();
		} catch (IOException e) {
			response = Response.status(Status.BAD_REQUEST).entity("Could not read files provided. Are they both PDF?").build();
		}

		return response;
	}

	
}
