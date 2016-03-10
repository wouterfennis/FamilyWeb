package servlets.FamilyWeb;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.FamilyWeb.Client;
import domain.FamilyWeb.Contact;
import domain.FamilyWeb.Survey;
import domain.FamilyWeb.User;

/**
 * Servlet implementation class ContactServlet
 */
public class ContactServlet extends HttpServlet {
	public static final String[] CONTACTGROUPS = {"household", "family", "friends",
			"colleagues", "neighbours", "acquaintance", "education",
			"club", "religion", "careinstitution", "youthcare",
			"bureauhalt", "justice"};
	private static final long serialVersionUID = 1L;
	public static final String SOCIALWORKER_FAMILY_NEW_NETWORK_CONTACTS_JSP = "/socialworker/family/new_network_contacts.jsp";
	public static final String SOCIALWORKER_FAMILY_NEW_NETWORK_QUESTIONS_JSP = "/socialworker/family/new_network_questions.jsp";
	public static final String CANT_LOAD_QUESTIONS = "Kon de vragenlijst niet inladen.";
	public static final String ENTER_AT_LEAST_ONE_CONTACT = "Voer minimaal één contact in voor de vragenlijst.";
	public static final String WRONG_DATA_WITH_CONTACTS = "Gegevens kloppen niet van één of meerdere contacten.";
	private User user = null;


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// get the user from the session
		user = (User) req.getSession().getAttribute("user");
		RequestDispatcher reqDisp = null;
		boolean contactsAreValid = false;
		ArrayList<Contact> contacts = null;		
		if (user != null) {
			contactsAreValid = true;
			// define hardcoded groups
			contacts = new ArrayList<Contact>();
			int j =0;
			int idOfContact = 1;
			// validate each group
			contactsAreValid = validateContacts(req, contactsAreValid, contacts, j, idOfContact);
		}
		// check if there are contacts created
		if(contacts.isEmpty()){
			contactsAreValid = false;
		}
		// if contacts are created, set survey attributes
		if (contactsAreValid) {
			// get interviewee
			String interviewee = req.getParameter("interviewee");
			String[] parts = interviewee.split(":");
			String nameInterviewee = parts[0];
			int IDOfInterviewee = Integer.parseInt(parts[1]);
			// get client from session
			Client client = (Client) req.getSession().getAttribute("client");
			// if interviewee is the one that was selected, set that into the session
			if (client.getClient_id() == IDOfInterviewee
					&& client.getForename().equals(nameInterviewee)) {
				req.getSession().setAttribute("intervieweeC", IDOfInterviewee);
				req.getSession().setAttribute("intervieweeF", 0);
			} 
			// if interviewee is an familymember, set that into the session
			else {
				req.getSession().setAttribute("intervieweeF", IDOfInterviewee);
				req.getSession().setAttribute("intervieweeC", 0);
			}
			// setup the survey from the select box
			String surveyName = req.getParameter("survey");
			Survey survey = user.getDbController().getSurvey(surveyName);
			if (!contactsAreValid && survey == null) {
				// set error message, because survey wasn't loaded well and return user to contacts page
				req.setAttribute("message", CANT_LOAD_QUESTIONS);
				req.setAttribute("messageType", "error");
				reqDisp = req
						.getRequestDispatcher(SOCIALWORKER_FAMILY_NEW_NETWORK_CONTACTS_JSP);
			} 
			// send user to next page for survey
			else if (contactsAreValid) {
				req.getSession().setAttribute("survey", survey);				
				reqDisp = req
						.getRequestDispatcher(SOCIALWORKER_FAMILY_NEW_NETWORK_QUESTIONS_JSP);
			}
			req.getSession().setAttribute("contacts", contacts);
		} 
		// no contact was created. Throw warning
		else if(contacts.isEmpty()){
			req.setAttribute("message",
					ENTER_AT_LEAST_ONE_CONTACT);
			req.setAttribute("messageType", "warning");
			reqDisp = req
					.getRequestDispatcher(SOCIALWORKER_FAMILY_NEW_NETWORK_CONTACTS_JSP);
		}
		// not all fields have been filled. Throw warning
		else {
			req.setAttribute("message",
					WRONG_DATA_WITH_CONTACTS);
			req.setAttribute("messageType", "warning");
			reqDisp = req
					.getRequestDispatcher(SOCIALWORKER_FAMILY_NEW_NETWORK_CONTACTS_JSP);

		}
		reqDisp.forward(req, resp);
	}

	private boolean validateContacts(HttpServletRequest req, boolean b, ArrayList<Contact> contacts, int j, int id) {
		for (String group : CONTACTGROUPS) {
            j++;
            // get amount of total created contacts in the group
            int contactsInGroup = Integer.parseInt((req.getParameter(
                    "counter" + group)));
            // for each contact created of group, get each input
            for (int i = 1; i <= contactsInGroup; i++) {
                // check if the contact has to be created
                String validate = req.getParameter(group + "validate" + i)
                        .trim();
                // contact has to be created
                if (validate.equals("true")) {
                    // get all input
                    String name = req.getParameter(group + "name" + i)
                            .trim();
                    String commentary = "";
                    String com = req.getParameter(group + "comment" + i);
                    if(com != null && !com.trim().equals(""))
                        commentary = com.trim();
                    String role = req.getParameter(group + "role" + i)
                            .trim();
                    // convert age from input
                    String ageS = req.getParameter(group + "age" + i)
                            .trim();
                    int age = 0;
                    try {
                        age = Integer.valueOf(ageS);
                    } catch (NumberFormatException e) {
                    }
                    // validate contact, if it went well create contact
					Contact c = new Contact();
                    if (c.checkContact(name, role, age)){
                        c = new Contact(name, commentary, role,
                                age, group,j);
                        c.setContact_id(id);
                        id++;
                        contacts.add(c);
                    }else
                        b = false;
                }
            }
        }
		return b;
	}
}
