#import "AppDelegate.h"
#import "MenuViewController.h"

@interface AppDelegate ()

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
        self.window = [[UIWindow alloc] initWithFrame:UIScreen.mainScreen.bounds];
    
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    MenuViewController *menuVC = [storyboard instantiateViewControllerWithIdentifier:@"MenuViewControllerID"];
    UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:menuVC];
    self.window.rootViewController = nav;
    [self.window makeKeyWindow];
    return YES;
}

- (void)applicationWillTerminate:(UIApplication *)application {
    
}

#pragma mark - UISceneSession lifecycle

- (UIInterfaceOrientationMask)application:(UIApplication *)application supportedInterfaceOrientationsForWindow:(UIWindow *)window {
    if (self.screenOrientation == ScreenOrientationLandscapeAndPortrait) {
        // Support Landscape Portrait
        return UIInterfaceOrientationMaskLandscape | UIInterfaceOrientationMaskPortrait;
    } else if (self.screenOrientation == ScreenOrientationLandscape) {
        // Support Landscape
        return UIInterfaceOrientationMaskLandscape;
    } else {
        // Support Portrait
        return UIInterfaceOrientationMaskPortrait;
    }
}


@end
