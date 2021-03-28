package main;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import main.Classes.classClass;
import main.UserSchedule.Day;
import main.UserSchedule.Meeting;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class DOMreadXML {
	
	private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s*", Pattern.DOTALL);

    private static void removeWhitespace(Document doc) {
        LinkedList<NodeList> stack = new LinkedList<>();
        stack.add(doc.getDocumentElement().getChildNodes());
        while (!stack.isEmpty()) {
            NodeList nodeList = stack.removeFirst();
            for (int i = nodeList.getLength() - 1; i >= 0; --i) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.TEXT_NODE) {
                    if (WHITESPACE_PATTERN.matcher(node.getTextContent()).matches()) {
                        node.getParentNode().removeChild(node);
                    }
                } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                    stack.add(node.getChildNodes());
                }
            }
        }
    }
    
	
	public static Boolean login(String email, String password) {
		
		try {

		    File fXmlFile = new File("src/main/gooberDatabase.xml");
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(fXmlFile);
		            
		    //optional, but recommended
		    //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		    doc.getDocumentElement().normalize();
		            
		    NodeList nList = doc.getElementsByTagName("User");
		    
		    for (int temp = 0; temp < nList.getLength(); temp++) {

		        Node nNode = nList.item(temp);
		                
		                
		        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

		            Element eElement = (Element) nNode;
		            if (email.equals(eElement.getElementsByTagName("email").item(0).getTextContent())) {
		            	if (password.equals(eElement.getElementsByTagName("password").item(0).getTextContent())) {
		            		return true;
		            	}
		            	else
		            		return false;
		            }
		        }
		    }
		    } catch (Exception e) {
		    e.printStackTrace();
		    }
		
		return false;
		
	}
	
public static User returnUser(String email) {
		
		try {
			
		    File fXmlFile = new File("src/main/gooberDatabase.xml");
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(fXmlFile);
		            
		    //optional, but recommended
		    //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		    
		    removeWhitespace(doc);
		    
		    doc.getDocumentElement().normalize();
		    
		    removeWhitespace(doc);
		            
		    NodeList nList = doc.getElementsByTagName("User");
		    
		    for (int temp = 0; temp < nList.getLength(); temp++) {
		    	
		        Node nNode = nList.item(temp);
		                
		                
		        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		        	
		            Element eElement = (Element) nNode;
		            if (email.equals(eElement.getElementsByTagName("email").item(0).getTextContent())) {
		            	String firstName = eElement.getElementsByTagName("firstName").item(0).getTextContent();
		            	String lastName = eElement.getElementsByTagName("lastName").item(0).getTextContent();
		            	String password = eElement.getElementsByTagName("password").item(0).getTextContent();
		            	String role = eElement.getElementsByTagName("role").item(0).getTextContent();
		            	
		            	Ratings ratings = new Ratings();
		            	ratings.numOfRatings = Integer.valueOf(eElement.getElementsByTagName("ratings").item(0).getAttributes().getNamedItem("numOf").getTextContent());
		            	
		            	Classes classes = new Classes();
		            	
		            	NodeList classNodeList = eElement.getElementsByTagName("class");
		            	int testCounter = 0;
		            	for (int temp2 = 0; temp2 < classNodeList.getLength(); temp2++) {
			            	classClass newClass = new classClass();
		            		Node classNode = classNodeList.item(temp2);
		            		
		            		if (classNode.getNodeType() == Node.ELEMENT_NODE) {
		            			
		            			Element classElement = (Element) classNode;
		            			
		            			newClass.className = classElement.getTextContent();
		            			newClass.grade = classElement.getAttribute("grade");
		            			classes.classList.add(newClass);
		            		}
		            	}
		            	
		            	Integer newRating;
		            	NodeList ratingNodeList = eElement.getElementsByTagName("rating");
		            	for (int temp3 = 0; temp3 < ratingNodeList.getLength(); temp3++) {
		            		Node ratingNode = ratingNodeList.item(temp3);
		            		
		            		if (ratingNode.getNodeType() == Node.ELEMENT_NODE) {
		            			
		            			Element ratingElement = (Element) ratingNode;
		            			
		            			newRating = Integer.valueOf(ratingElement.getTextContent());
		            			ratings.ratingList.add(newRating);
		            		}
		            	}
		            	
		            	UserSchedule schedule = new UserSchedule();

		            	NodeList dayNodeList = eElement.getElementsByTagName("day");
		            	for (int temp4 = 0; temp4 < dayNodeList.getLength(); temp4++) {
			            	Day newDay = new Day();
		            		Node dayNode = dayNodeList.item(temp4);
		            		//System.out.println(dayNodeList.item(temp4).getNodeName());
		            		//System.out.println("131");
		            		if (dayNode.getNodeType() == Node.ELEMENT_NODE) {
		            			//System.out.println("133");
		            			
		            			Element dayElement = (Element) dayNode;
		            			
		            			newDay.dayName = dayElement.getElementsByTagName("dayText").item(0).getTextContent();
//		            			newDay.dayName = dayNode.getFirstChild().getNodeValue();
//		            			System.out.println("OLD WAY " + newDay.dayName);
		            			System.out.println("NEW WAY " + dayElement.getElementsByTagName("dayText").item(0).getTextContent());
		            			
		            			NamedNodeMap attr = dayNode.getAttributes();
				                Node dayAttr = attr.getNamedItem("availability");
				                newDay.availability = dayAttr.getTextContent();
//				                System.out.println("137 " + dayAttr.getTextContent());
		            			
				                NodeList meetingNodeList = eElement.getElementsByTagName("meetingWith");
				                for (int temp5 = 0; temp5 < meetingNodeList.getLength(); temp5++) {
					            	Meeting newMeeting = new Meeting();
				                	Node meetingNode = meetingNodeList.item(temp5);
				                	
				                	if (meetingNode.getNodeType() == Node.ELEMENT_NODE) {
				                		Element meetingElement = (Element) meetingNode;
				                		newMeeting.meetingWith = meetingElement.getTextContent();
				                		newMeeting.classCode = meetingElement.getAttribute("meetingWith");
				                		newDay.meetingList.add(newMeeting);
				                	}
				                }
				                
		            		}
		            		schedule.week[temp4] = newDay;
		            	}
		            	
//		            	schedule.week[0].dayName = "Monday";
//		            	schedule.week[1].dayName = "Tuesday";
//		            	schedule.week[2].dayName = "Wednesday";
//		            	schedule.week[3].dayName = "Thursday";
//		            	schedule.week[4].dayName = "Friday";
//		            	schedule.week[5].dayName = "Saturday";
//		            	schedule.week[6].dayName = "Sunday";
		            	
		            	User user1 = new User.userBuilder(email, firstName, lastName, password, role, ratings, classes, schedule).build();
		            	return user1;
		            }
		        }
		    }
		    } catch (Exception e) {
		    e.printStackTrace();
		    }
		
		return null;
		
	}

public static User[] tutorsInClass(String classCode) {
	User[] tutorList = new User[100];
	int listIndex = 0;
	
	try {

	    File fXmlFile = new File("src/main/gooberDatabase.xml");
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(fXmlFile);

	    doc.getDocumentElement().normalize();
	            
	    NodeList nList = doc.getElementsByTagName("User");
	            
	    for (int temp = 0; temp < nList.getLength(); temp++) {

	        Node nNode = nList.item(temp);
	                
	        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

	            Element eElement = (Element) nNode;

	            if (eElement.getElementsByTagName("role").item(0).getTextContent().equals("Tutor")) {
	            	NodeList nList2 = eElement.getElementsByTagName("class");
	    	        
	    	        
	    	        for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {

	    		        Node nNode2 = nList2.item(temp2);
	    		        
	    		        if (nNode2.getNodeType() == Node.ELEMENT_NODE) {

	    		            Element eElement2 = (Element) nNode2;
	    		            if (eElement2.getTextContent().equals(classCode)) {
	    		            	tutorList[listIndex] = returnUser(eElement.getElementsByTagName("email").item(0).getTextContent());
	    		            	listIndex++;
	    		            }
	    		        }
	    		    }

	            }


	        }
	    }
	    } catch (Exception e) {
	    e.printStackTrace();
	    }
	
	
	return tutorList;
}

public static User[] listOfTutors() {
	User[] tutorList = new User[100];
	int listIndex = 0;
	
	try {

	    File fXmlFile = new File("src/main/gooberDatabase.xml");
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(fXmlFile);

	    doc.getDocumentElement().normalize();
	            
	    NodeList nList = doc.getElementsByTagName("User");
	            
	    for (int temp = 0; temp < nList.getLength(); temp++) {

	        Node nNode = nList.item(temp);
	                
	        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

	            Element eElement = (Element) nNode;

	            if (eElement.getElementsByTagName("role").item(0).getTextContent().equals("Tutor")) {
	            	tutorList[listIndex] = returnUser(eElement.getElementsByTagName("email").item(0).getTextContent());
	            	listIndex++;
	            }


	        }
	    }
	    } catch (Exception e) {
	    e.printStackTrace();
	    }
	
	
	return tutorList;
}
	
public static float round(float d, int decimalPlace){
	if(d == 0)
	{
		return 0;
	}
	return BigDecimal.valueOf(d).setScale(decimalPlace,BigDecimal.ROUND_HALF_UP).floatValue();     
}	
	
public static float avgRating(String email) {
	float retAvg = 0;
		try {
			
		    File fXmlFile = new File("src/main/gooberDatabase.xml");
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(fXmlFile);
		            
		    //optional, but recommended
		    //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		    doc.getDocumentElement().normalize();
		            
		    NodeList nList = doc.getElementsByTagName("User");
		    
		    
		    for (int temp = 0; temp < nList.getLength(); temp++) {

		        Node nNode = nList.item(temp);
		                
		                
		        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

		            Element eElement = (Element) nNode;
		            if (email.equals(eElement.getElementsByTagName("email").item(0).getTextContent())) {
		    	        Node Ratings = eElement.getElementsByTagName("ratings").item(0);
		    	        NodeList nList2 = eElement.getElementsByTagName("rating");
		    	        
		    	        
		    	        for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {

		    		        Node nNode2 = nList2.item(temp2);
		    		                
		    		                
		    		        if (nNode2.getNodeType() == Node.ELEMENT_NODE) {

		    		            Element eElement2 = (Element) nNode2;
		    		            retAvg += Integer.valueOf(eElement2.getTextContent());
		    		        }
		    		    }
		    	        
		    	        
		    	        
		            	NamedNodeMap attr = Ratings.getAttributes();
		                Node nodeAttr = attr.getNamedItem("numOf");
		    	        retAvg = retAvg / Integer.valueOf(nodeAttr.getTextContent());
		    	        //return round(retAvg,2);
		    	        return retAvg;
		            }
		        }
		    }
		    
		    //return round(retAvg,2);
		    return retAvg;
		    
		    
		    
		    
		    } catch (Exception e) {
		    e.printStackTrace();
		    }
		//return round(retAvg,2);
		return retAvg;
	}
}
