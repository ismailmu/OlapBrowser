package org.imu.olapbrowser.utility;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.imu.olapbrowser.domain.DataField;
import org.imu.olapbrowser.domain.DataJson;
import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.Position;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Member;
import org.olap4j.metadata.NamedList;

import com.google.gson.Gson;

public class OlapUtility {

	private static OlapUtility instance = null;
	private static OlapConnection connection = null;

	public static OlapUtility getInstance() {
		if (instance == null) {
			instance = new OlapUtility();
		}
		return instance;
	}

	private OlapUtility() {
		try {
			//harus setting dulu iis nya dan iisnya harus nyala
			Class.forName("org.olap4j.driver.xmla.XmlaOlap4jDriver");
			connection = (OlapConnection) DriverManager.getConnection(
			// This is the SQL Server service end point.
					"jdbc:xmla:Server=http://localhost/OLAP/msmdpump.dll"

					// Tells the XMLA driver to use a SOAP request cache layer.
					// We will use an in-memory static cache.
					+ ";Cache=org.olap4j.driver.xmla.cache.XmlaOlap4jNamedMemoryCache"

					// Sets the cache name to use. This allows
					// cross-connection
					// cache sharing. Don't give the driver a cache name
					// and it
					// disables sharing.
					+ ";Cache.Name=MyNiftyConnection"

					// Some cache performance tweaks.
					// Look at the javadoc for details.
					+ ";Cache.Mode=LFU;Cache.Timeout=600;Cache.Size=100");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String GetCubes() {
		if (connection != null) {
			List<String> cubeString=new ArrayList<String>();
			try {
				NamedList<Cube> cubes = connection.getOlapSchema().getCubes();
				for (Cube cube : cubes) {
					cubeString.add(cube.getName());
				}
			} catch (OlapException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return new Gson().toJson(cubeString);
		} else {
			return "";
		}
	}
	
	public String getDataMdx(String mdx) {
		
		DataJson dataJson = new DataJson();
		try {
			final CellSet cellSet = connection.createStatement().executeOlapQuery(mdx);
			
			final CellSetAxis rowsAxis = cellSet.getAxes().get(Axis.ROWS.axisOrdinal());
			final CellSetAxis colAxis = cellSet.getAxes().get(Axis.COLUMNS.axisOrdinal());
			
			dataJson.setField(getDataField(rowsAxis, colAxis));
			dataJson.setContent(getContent(cellSet, rowsAxis, colAxis));
			dataJson.setContentAll(getContent(cellSet, rowsAxis, colAxis, getHeader(rowsAxis, colAxis)));
			dataJson.setIsValid(true);
			dataJson.setMessage("");
			
		} catch (OlapException e) {
			dataJson.setIsValid(false);
			dataJson.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return new Gson().toJson(dataJson);
		
	}
	
	protected void setData(List<List<Object>> list,CellSet cellSet,CellSetAxis rowsAxis,CellSetAxis colAxis) {
		for (int i=0;i<rowsAxis.getPositionCount();i++) {
			Position rowPos = rowsAxis.getPositions().get(i);
			
			List<Member> members = rowPos.getMembers();
			List<Object> objData = new ArrayList<Object>();
			
			for (Member member : members) {
				
				int depth = (member.getDepth() == 0) ?1:member.getDepth();
				
				Member[] memberLength = new Member[depth];
				
				Member memberTemp=member;
				memberLength[0] = memberTemp;
				
				for(int j=1;j<memberLength.length;j++) {
					memberLength[j]=memberTemp.getParentMember();
					memberTemp=memberLength[j];
				}
				
				for(int j=memberLength.length-1;j>=0;j--) {
					objData.add(memberLength[j].getName());
				}
				
			}
			
			for (Position colPos : colAxis) {
				Object val = cellSet.getCell(colPos,rowPos).getValue();
				objData.add(val);
			}
			list.add(objData);
		}
	}
	
	protected List<List<Object>> getContent(CellSet cellSet,CellSetAxis rowsAxis,CellSetAxis colAxis,List<Object> header) {
		List<List<Object>> list = new ArrayList<List<Object>>();
		list.add(header);
		setData(list, cellSet, rowsAxis, colAxis);
		
		return list;
	}
	
	protected List<List<Object>> getContent(CellSet cellSet,CellSetAxis rowsAxis,CellSetAxis colAxis) {
		List<List<Object>> list = new ArrayList<List<Object>>();
		setData(list, cellSet, rowsAxis, colAxis);
		return list;
	}
	
	protected List<Object> getHeader(CellSetAxis rowsAxis,CellSetAxis colAxis) {
		
		List<Object> list = new ArrayList<Object>();
		for (Member member : rowsAxis.getPositions().get(0).getMembers()) {
			
			int depth = (member.getDepth() == 0) ?1:member.getDepth();
			
			Member[] memberLength = new Member[depth];
			
			Member memberTemp=member;
			memberLength[0] = memberTemp;
			
			for(int j=1;j<memberLength.length;j++) {
				memberLength[j]=memberTemp.getParentMember();
				memberTemp=memberLength[j];
			}
			
			for(int j=memberLength.length-1;j>=0;j--) {
				Member memberRow = memberLength[j];
				list.add(memberRow.getLevel().getName());
			}
		}
		
		for (Position colPos : colAxis) {
			for (Member memberCol : colPos.getMembers()) {
				list.add(memberCol.getName());
			}
		}
		
		return list;
	}
	
	protected List<DataField> getDataField(CellSetAxis rowsAxis,CellSetAxis colAxis) {
		
		List<DataField> list = new ArrayList<DataField>();
		for (Member member : rowsAxis.getPositions().get(0).getMembers()) {
			
			int depth = (member.getDepth() == 0) ?1:member.getDepth();
			
			Member[] memberLength = new Member[depth];
			
			Member memberTemp=member;
			memberLength[0] = memberTemp;
			
			for(int j=1;j<memberLength.length;j++) {
				memberLength[j]=memberTemp.getParentMember();
				memberTemp=memberLength[j];
			}
			
			for(int j=memberLength.length-1;j>=0;j--) {
				Member memberRow = memberLength[j];
				list.add(new DataField("string", memberRow.getLevel().getName()));
			}
		}
		
		for (Position colPos : colAxis) {
			for (Member memberCol : colPos.getMembers()) {
				list.add(new DataField("number", memberCol.getName()));
			}
		}
		
		return list;
	}
}
