package wxx.filesplitter;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.util.JournalLogger;
import com.wm.app.b2b.server.ISRuntimeException;
import com.softwareag.util.IDataMap;
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class utils

{
	// ---( internal utility methods )---

	final static utils _instance = new utils();

	static utils _newInstance() { return new utils(); }

	static utils _cast(Object o) { return (utils)o; }

	// ---( server methods )---




	public static final void splitAndUpload (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(splitAndUpload)>> ---
		// @sigtype java 3.5
		// [i] object:0:optional contentStream
		// [i] field:0:required destinationFolder
		// [i] field:0:required fileName
		// [i] field:0:required destinationFileSize
		// [o] field:0:required numberOfSplittedFiles
		IDataMap pipeMap = new IDataMap(pipeline);
		InputStream contentStream = (InputStream) pipeMap.get("contentStream");
		String destinationFolder = pipeMap.getAsString("destinationFolder");
		String fileName = pipeMap.getAsString("fileName");
		log("start splitAndUpload for: " + fileName);
		String sessionKey = pipeMap.getAsString("sessionKey");
		int destinationFileSize = Integer.valueOf(pipeMap.getAsString("destinationFileSize"));
		
		byte[] buffer = new byte[destinationFileSize];
		int bytesRead;
		int i = 0;
		
		try {
		while ((bytesRead = contentStream.read(buffer)) != -1) {
		log("while");
		InputStream splittedContentStream = new ByteArrayInputStream(buffer, 0, bytesRead);
		IDataMap inputMap = new IDataMap();
		inputMap.put("sessionKey", sessionKey);
		inputMap.put("destinationFolder", destinationFolder);
		inputMap.put("fileNameNumbered", fileName + "." + i);
		inputMap.put("splittedContentStream", splittedContentStream);
		
		Service.doInvoke("wxx.filesplitter.utils", "upload", inputMap.getIData());
		splittedContentStream.close();
		i++;
		}
		} catch (Exception e) {
		throw new ServiceException(e);
		}
		pipeMap.put("numberOfSplittedFiles", ""+i);
		log("end splitAndUpload");
		// --- <<IS-END>> ---

                
	}



	public static final void throwError (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(throwError)>> ---
		// @sigtype java 3.5
		// [i] field:0:required msg
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		
		String	msg = IDataUtil.getString( pipelineCursor, "msg" );
		pipelineCursor.destroy();
		
		throw new ServiceException(msg);			
			
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	public static final class DocumentDiscardedException extends ServiceException {
		
		public DocumentDiscardedException(String arg0) {
			super(arg0);
		}
		
		private static final long serialVersionUID = 1L;
	
	}		
	
	public static final class DocumentDiscardedWithWarningException extends ServiceException {
		
		public DocumentDiscardedWithWarningException(String arg0) {
			super(arg0);
		}
		
		private static final long serialVersionUID = 1L;
	
	}			
		
	private static void log(String message) {
		JournalLogger.log(4, JournalLogger.FAC_FLOW_SVC,  JournalLogger.INFO, "splitFilesInFolder", message);
		 
	}
		
		
		
		
	// --- <<IS-END-SHARED>> ---
}

