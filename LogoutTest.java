package LoginAndLogoutTests;

import Cobalt_BusinessComponents.Logout_Components;
import org.testng.annotations.Test;

import java.util.Properties;

public class LogoutTest {

    Logout_Components logout_components = new Logout_Components();

    @Test(priority = 2)
    public void verifyLogout(Properties prop, String userRole) throws Exception {
        logout_components.logOutFromCobalt();
    }
}
