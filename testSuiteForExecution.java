package executeEngine;

import LoginAndLogoutTests.LogoutTest;
import LoginAndLogoutTests.loginTest;


import java.util.Properties;

public class testSuiteForExecution {


    loginTest loginT ;
    LogoutTest logoutT;

    public testSuiteForExecution() throws Exception {
        loginT = new loginTest();
        logoutT = new LogoutTest();
    }

    public void verifYLoginToCobalt(Properties prop, String userRole) throws Exception {
        loginT.verifyLogin(prop, userRole);
    }


    public void verifyLogoutFromCobalt(Properties prop, String userRole) throws Exception {
        logoutT.verifyLogout(prop, userRole);
    }



    public void verifyLoginWithInvalidUn(Properties prop, String userRole) throws Exception {
        loginT.verifyLoginUsingInvalidUn(prop, userRole);
    }


}
