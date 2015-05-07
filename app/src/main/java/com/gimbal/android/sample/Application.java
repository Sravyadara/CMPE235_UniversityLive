package com.gimbal.android.sample;

import com.gimbal.android.Gimbal;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by sravyadara on 5/6/15.
 */
public class Application extends android.app.Application {

    public Application(){ }

    @Override
    public void onCreate(){
        super.onCreate();

        Gimbal.setApiKey(this, "9da1beb6-724c-4735-9e0c-e8dbabe0bdb4");
        ParseObject.registerSubclass(RegistrationDAO.class);
        Parse.initialize(this, "ZvV2teapMUKUJPsYJ6fqIRQJUgiF3syAU012RQe4", "pLIGQb9jQW9ZcwGaD8LAvIFfcdX7gpRYf82FAYzy");
    }

}
