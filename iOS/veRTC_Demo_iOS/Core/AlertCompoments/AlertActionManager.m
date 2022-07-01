//
//  AlertActionManager.m
//  quickstart

#import "AlertActionManager.h"
#import "DeviceInforTool.h"
#import <UIKit/UIKit.h>

@interface AlertActionManager ()

@property (nonatomic, weak) UIAlertController *alert;
@property (nonatomic, strong) NSTimer *timer;
 
@end

@implementation AlertActionManager

+ (AlertActionManager *)shareAlertActionManager {
    static dispatch_once_t onceToken;
    static AlertActionManager *alertActionManager;
    dispatch_once(&onceToken, ^{
        alertActionManager = [[AlertActionManager alloc] init];
    });
    return alertActionManager;
}

#pragma mark - Publish Action

- (void)showWithMessage:(NSString *)message {
    [self showWithMessage:message actions:nil];
}

- (void)showWithMessage:(NSString *)message actions:(NSArray<AlertActionModel *> *)actions {
    [self showWithMessage:message actions:actions hideDelay:0];
}

- (void)showWithMessage:(NSString *)message actions:(NSArray<AlertActionModel *> *)actions hideDelay:(NSTimeInterval)delay {
    if (message.length <= 0 || _alert != nil) {
        return;
    }
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"" message:message preferredStyle:UIAlertControllerStyleAlert];
    for (int i = 0; i < actions.count; i++) {
        AlertActionModel *model = actions[i];
        UIAlertAction *alertAction = [UIAlertAction actionWithTitle:model.title style:UIAlertActionStyleDefault handler:^(UIAlertAction *_Nonnull action) {
            [self stopTimer];
            if (model.alertModelClickBlock) {
                model.alertModelClickBlock(action);
            }
        }];
        [alert addAction:alertAction];
    }
    __weak __typeof(self) wself = self;
    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController *rootVC = [DeviceInforTool topViewController];
        [rootVC presentViewController:alert animated:YES completion:nil];
        wself.alert = alert;
        [wself startTimer:delay];
    });
}

- (void)dismiss:(void (^)(void))completion {
    if (_alert) {
        [self stopTimer];
        [_alert dismissViewControllerAnimated:YES completion:^{
            if (completion) {
                completion();
            }
        }];
    } else {
        if (completion) {
            completion();
        }
    }
}

- (void)dismiss {
    if (_alert) {
        [self stopTimer];
        [_alert dismissViewControllerAnimated:YES completion:^{

        }];
    }
}

- (void)startTimer:(NSTimeInterval)interval {
    [self stopTimer];
    if (interval <= 0) {
        return;
    }
    self.timer = [NSTimer timerWithTimeInterval:interval target:self selector:@selector(dismiss) userInfo:nil repeats:NO];
    [[NSRunLoop mainRunLoop] addTimer:self.timer forMode:NSRunLoopCommonModes];
}

- (void)stopTimer {
    if (self.timer) {
        [self.timer invalidate];
        self.timer = nil;
    }
}

@end
