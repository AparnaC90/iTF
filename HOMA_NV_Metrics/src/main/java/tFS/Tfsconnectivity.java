package tFS;

	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileNotFoundException;
	import java.io.FileOutputStream;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.net.MalformedURLException;
	import java.net.URI;
	import java.net.URISyntaxException;
	import java.net.URL;
	import java.nio.file.Path;
	import java.text.SimpleDateFormat;
	import java.util.Date;

	import javax.swing.JTable;
	import javax.swing.table.TableModel;
	import javax.xml.parsers.DocumentBuilder;

	import org.apache.poi.ss.usermodel.Cell;
	import org.apache.poi.ss.usermodel.Row;
	import org.apache.poi.xssf.usermodel.XSSFSheet;
	import org.apache.poi.xssf.usermodel.XSSFWorkbook;
	import org.hsqldb.Table;
	import org.hsqldb.util.CSVWriter;

	import com.microsoft.tfs.core.TFSTeamProjectCollection;
	import com.microsoft.tfs.core.clients.workitem.CoreFieldReferenceNames;
	import com.microsoft.tfs.core.clients.workitem.WorkItem;  
	import com.microsoft.tfs.core.clients.workitem.WorkItemClient;
	import com.microsoft.tfs.core.clients.workitem.WorkItemStateListener;
	import com.microsoft.tfs.core.clients.workitem.exceptions.UnableToSaveException;
	import com.microsoft.tfs.core.clients.workitem.fields.FieldCollection;
	import com.microsoft.tfs.core.clients.workitem.files.AttachmentCollection;
	import com.microsoft.tfs.core.clients.workitem.link.LinkCollection;
	import com.microsoft.tfs.core.clients.workitem.project.Project;

	import com.microsoft.tfs.core.clients.workitem.query.WorkItemCollection;
	import com.microsoft.tfs.core.clients.workitem.revision.RevisionCollection;
	import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;
	import com.microsoft.tfs.core.httpclient.Credentials;
	import com.microsoft.tfs.core.httpclient.UsernamePasswordCredentials;
	import com.microsoft.tfs.core.httpclient.util.URIUtil;
	import com.microsoft.tfs.core.util.URIUtils;

import wrappers.iTF;

import com.microsoft.tfs.core.clients.*;

	import java.awt.BorderLayout;

	import javax.swing.JFrame;
	import javax.swing.JScrollPane;

	 public class Tfsconnectivity 
	 {
	
		public static TFSTeamProjectCollection connectToTFS() 
		{
			TFSTeamProjectCollection tpc=null;
			Credentials credentials;
			
			credentials = new UsernamePasswordCredentials("nkarthikeyan","karth!gmi123");
			tpc = new TFSTeamProjectCollection(URIUtils.newURI("http://10.0.10.79:8080/tfs/DefaultCollection"), credentials);
		    return tpc;
		}
	}


