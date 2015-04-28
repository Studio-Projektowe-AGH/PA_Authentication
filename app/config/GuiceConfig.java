package config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import controllers.SigninController;
import controllers.SignupController;
import controllers.SocialSigninController;
import models.LoginCredentials;
import models.SocialCredentials;
import org.bson.types.ObjectId;
import services.authentication.BasicAuthenticationService;
import services.authentication.FacebookAuthenticationService;
import services.authentication.UserAuthenticationService;
import services.data.BasicDataService;
import services.data.FacebookDataServiceProvider;
import services.data.UserDataSourceProvider;


/**
 * Created by Wojtek on 21/04/15.
 */
public class GuiceConfig  extends AbstractModule {

    @Override
    public void configure() {
        bind(new TypeLiteral<BasicDataService<LoginCredentials, ObjectId>>() {}).toProvider(UserDataSourceProvider.class);
        bind(new TypeLiteral<BasicDataService<LoginCredentials, ObjectId>>(){}).annotatedWith(Names.named("SocialDataService")).toProvider(FacebookDataServiceProvider.class);
        bind(new TypeLiteral<BasicAuthenticationService<LoginCredentials>>(){}).to(UserAuthenticationService.class);
        bind(new TypeLiteral<BasicAuthenticationService<SocialCredentials>>(){}).to(FacebookAuthenticationService.class);

        requestStaticInjection(UserAuthenticationService.class);
        requestStaticInjection(FacebookAuthenticationService.class);
        requestStaticInjection(SigninController.class);
        requestStaticInjection(SignupController.class);
        requestStaticInjection(SocialSigninController.class);
    }
}
