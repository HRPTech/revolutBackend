package com.revolut;

import org.jooby.Jooby;
import org.jooby.json.Gzon;

import com.revolut.controller.AccountController;
import com.revolut.controller.BalanceController;
import com.revolut.controller.TransactionController;

public class Application extends Jooby {

    {
        use(new Gzon());
        use(TransactionController.class);
        use(BalanceController.class);  
        use(AccountController.class);  
    }

    public static void main(final String[] args) {    	
        run(Application::new, args);
    }
}
