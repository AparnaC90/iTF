package tFS;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.workitem.CoreFieldReferenceNames;
import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.project.Project;
import com.microsoft.tfs.core.clients.workitem.wittype.WorkItemType;
import com.microsoft.tfs.core.httpclient.Credentials;
import com.microsoft.tfs.core.httpclient.UsernamePasswordCredentials;
import com.microsoft.tfs.core.util.URIUtils;

import wrappers.iTF;

public class Defectcreation extends iTF 
{
	public static void createBugsinTFS() throws IOException
	{
		String Priority = null,Severity=null,TestSteps=null,Expectedresult=null,ActualResult=null,Title=null;

		//TFSTeamProjectCollection tpc=null;
		System.setProperty("com.microsoft.tfs.jni.native.base-directory", "./tfssdk/TFS-SDK-11.0.0/redist/native");
		Credentials credentials;

		credentials = new UsernamePasswordCredentials("nkarthikeyan","karth!gmi123");
		TFSTeamProjectCollection tpc = new TFSTeamProjectCollection(URIUtils.newURI("http://10.0.10.79:8080/tfs/DefaultCollection"), credentials);
		//TFSTeamProjectCollection tpc=connectToTFS();
		System.out.println("");
		Project project = tpc.getWorkItemClient().getProjects().get("iTF");
		System.out.println("Project");
		System.out.println(project.getName());
		WorkItemType bugType = project.getWorkItemTypes().get("Bug");
		System.out.println(bugType.getName());

		//	I need to fetch the date from excel and no of rows
		FileInputStream file = new FileInputStream(new File("./reports/Defect/Defects.xlsx")); 
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0); 



		for(int i=1;i<sheet.getLastRowNum();i++)

		{
			if(sheet.getRow(i)!=null){

				sheet.getRow(i).getCell(2).setCellType(Cell.CELL_TYPE_STRING);

				Cell pri = sheet.getRow(i).getCell(2);
				Cell sev = sheet.getRow(i).getCell(3);
				Cell ts =sheet.getRow(i).getCell(0);
				Cell es =sheet.getRow(i).getCell(5);
				Cell as =sheet.getRow(i).getCell(6);
				Cell tle = sheet.getRow(i).getCell(1);

				if(pri!=null&& sev!=null && ts!=null && es!=null && as!=null && tle!=null ) {
					Priority =	pri.getStringCellValue();
					Severity = sev.getStringCellValue();
					TestSteps = ts.getStringCellValue();
					Expectedresult = es.getStringCellValue();
					ActualResult = as.getStringCellValue();
					Title = tle.getStringCellValue();
					
					WorkItem newWorkItem = project.getWorkItemClient().newWorkItem(bugType);
					newWorkItem.setTitle(Title);
					newWorkItem.getFields().getField(CoreFieldReferenceNames.AREA_PATH).setValue("iTF");
					//	System.out.println("Area Path");
					newWorkItem.getFields().getField(CoreFieldReferenceNames.ASSIGNED_TO).setValue("Venkateswara Reddy Gudugunur Rami");
					//	System.out.println("ASSIGNED TO");
					newWorkItem.getFields().getField(CoreFieldReferenceNames.ITERATION_PATH).setValue("iTF\\Iteration 1");
					//	System.out.println("Iteration path");
					newWorkItem.getFields().getField(CoreFieldReferenceNames.STATE).setValue("Active");
					//	System.out.println("State");
					//	newWorkItem.getFields().getField(CoreFieldReferenceNames.DESCRIPTION).setValue("Login to CSMS\n Go to Dashboard\n Click Add Application\n");
					//	System.out.println("Description");
					//	newWorkItem.getFields().getField(CoreFieldReferenceNames.HISTORY).setValue("Test History");
					newWorkItem.getFields().getField("Severity").setValue(Severity);
					newWorkItem.getFields().getField("Priority").setValue(Priority);
					//	newWorkItem.getFields().getField("Repro Steps").setValue(TestSteps+"\n"+Expectedresult+"\n"+ActualResult);
					
					

					newWorkItem.save();
					System.out.println(newWorkItem.getID());
					
					

				}
				System.out.println("End of for loop");
			}}
	}

}
