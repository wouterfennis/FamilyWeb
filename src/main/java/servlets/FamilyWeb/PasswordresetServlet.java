package servlets.FamilyWeb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import servletControllers.FamilyWeb.OverviewController;
import domain.FamilyWeb.Administrator;
import domain.FamilyWeb.Socialworker;
import domain.FamilyWeb.User;

@SuppressWarnings("serial")
public class PasswordresetServlet extends HttpServlet { 

	private HttpServletRequest req = null;
	
	private final String PAGE_LOGIN = "/login.jsp";
	private final String PAGE_PASSWORD_RESET = "/password_reset.jsp";
	private final String PAGE_STARTSCREEN_ADMINISTRATOR = "/administrator/startscreen_administrator.jsp";
	private final String PAGE_STARTSCREEN_SOCIALWORKER = "/socialworker/startscreen_socialworker.jsp";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
	
		this.req = request;
		// get the currentUser 
		User currentUser = getUser(request);
		
		// check if password is not null
		if (request.getParameter("old_password") != null && request.getParameter("new_password") != null && request.getParameter("new_password_confirmation") != null) {
			
			String oldPassword = request.getParameter("old_password").trim();
			String newPassword = request.getParameter("new_password").trim();
			String newPasswordConfirm = request.getParameter("new_password_confirmation").trim();
			
			// check if old password is valid and new password is equal to new password confirmation
			validatePassword(request, resp, currentUser, oldPassword, newPassword, newPasswordConfirm);

			// input not valid, password is null or empty
		} else {
			this.setMessage("warning", "Alle velden dienen ingevuld te worden.");
			request.getRequestDispatcher(PAGE_PASSWORD_RESET).forward(request, resp);
		}
	}

	private void validatePassword(HttpServletRequest request, HttpServletResponse resp, User currentUser, String oldPassword, String newPassword, String newPasswordConfirm) throws ServletException, IOException {
		if (oldPassword.equals(currentUser.getPassword()) && newPassword.equals(newPasswordConfirm)) {

            // Update user in database and set password reset to false
            currentUser.setPassword(newPassword);
            currentUser.setWwreset(false);
            currentUser.updateDB();

            // Refresh the user in session
            request.getSession().setAttribute("user", currentUser.getDbController().getUser(currentUser.getUsername()));
            this.setMessage("success", "Succesvol wachtwoord veranderd.");

            // Check if user is administrator
            if (currentUser instanceof Administrator) {
                setAdministratorClients(request, resp, currentUser);
                // User is socialworker
            } else {
                setSocialworkerClients(request, resp, currentUser);
            }

        // input not valid, password combination is wrong
        } else {
            this.setMessage("warning", "Wachtwoord onjuist of wachtwoorden komen niet overeeen.");
            request.getRequestDispatcher(PAGE_PASSWORD_RESET).forward(request, resp);
        }
	}

	private void setAdministratorClients(HttpServletRequest request, HttpServletResponse resp, User currentUser) throws ServletException, IOException {
		try {
        // load/set users and clients in overview tables
        request.getSession().setAttribute("usersJSON", OverviewController.getInstance().RefreshOverviewUsers(currentUser));
        request.getSession().setAttribute("clientsJSON", OverviewController.getInstance().RefreshOverviewClients(currentUser));
        // load/set users for autocomple client add/edit page
        request.getSession().setAttribute("users", OverviewController.getInstance().autoComplete(currentUser));
        request.getRequestDispatcher(PAGE_STARTSCREEN_ADMINISTRATOR).forward(request, resp);
        } catch (JSONException e) {
            this.setMessage("error", "Kon de gegevens niet goed inladen, probeer opnieuw in te loggen.");
            request.getRequestDispatcher(PAGE_LOGIN).forward(request, resp);
        }
	}

	private void setSocialworkerClients(HttpServletRequest request, HttpServletResponse resp, User currentUser) throws ServletException, IOException {
		try {
        // load/set clients in overview tables
        request.getSession().setAttribute("clientsJSON", OverviewController.getInstance().RefreshOverviewClients(currentUser));
        request.getSession().setAttribute("clients", currentUser.getDbController().getAllClientsOfUser(currentUser));
        request.getRequestDispatcher(PAGE_STARTSCREEN_SOCIALWORKER).forward(request, resp);
        } catch (JSONException e) {
            this.setMessage("error", "Kon de gegevens niet goed inladen, probeer opnieuw in te loggen.");
            request.getRequestDispatcher(PAGE_LOGIN).forward(request, resp);
        }
	}

	private User getUser(HttpServletRequest request) {
		Object currentUserObject = request.getSession().getAttribute("user");
		return (currentUserObject instanceof Administrator) ? (Administrator) currentUserObject : (Socialworker) currentUserObject;
	}

	/**
	 * Method to set information message on page.
	 * @param messageType String type of message could be success, error or warning
	 * @param message String the message to display
	 */
	private void setMessage(String messageType, String message) {
		req.setAttribute("messageType", messageType);
		req.setAttribute("message", message);
	}
}
