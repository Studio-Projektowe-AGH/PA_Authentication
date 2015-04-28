package config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import controllers.SigninController;
import controllers.SignupController;
import models.LoginCredentials;
import models.SocialCredentials;
import org.bson.types.ObjectId;
import services.*;


/**
 * Created by Wojtek on 21/04/15.
 */
public class GuiceConfig  extends AbstractModule {

    @Override
    public void configure() {
        bind(new TypeLiteral<BasicDataService<LoginCredentials, ObjectId>>(){}).toProvider(UserDataSourceProvider.class);
        bind(new TypeLiteral<BasicAuthenticationService<LoginCredentials>>(){}).to(UserAuthenticationService.class);
        bind(new TypeLiteral<BasicAuthenticationService<SocialCredentials>>(){}).to(FacebookAuthenticationService.class);

        requestStaticInjection(UserAuthenticationService.class);
        requestStaticInjection(FacebookAuthenticationService.class);
        requestStaticInjection(SigninController.class);
        requestStaticInjection(SignupController.class);
    }
}
