package com.seera.elaaoverlaypdf.service;

import java.io.InputStream;

import javax.ws.rs.core.Response;


/**
 * 
 * @author ricardopaulo
 *
 */
public interface OverlayDocumentServiceInterface {
	/**
	 * 
	 * @param etoDoc eto document from client (MOH)
	 * @param stamp picture to overlay in the document
	 * @return
	 */
	public Response uploadFile(InputStream etoDoc,  InputStream qrCodeDoc);
}
