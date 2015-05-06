package com.gimbal.android.sample;

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

        ParseObject.registerSubclass(RegistrationDAO.class);
        Parse.initialize(this, "ZvV2teapMUKUJPsYJ6fqIRQJUgiF3syAU012RQe4", "pLIGQb9jQW9ZcwGaD8LAvIFfcdX7gpRYf82FAYzy");
    }

}
