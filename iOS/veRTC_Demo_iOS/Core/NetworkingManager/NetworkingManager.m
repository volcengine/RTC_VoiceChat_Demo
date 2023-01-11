//
//  NetworkingManager.m
//  veRTC_Demo
//
//  Created by on 2021/12/16.
//
//

#import <AFNetworking/AFNetworking.h>
#import "NetworkingManager.h"
#import "NetworkingTool.h"
#import <YYModel/YYModel.h>
#import "BuildConfig.h"

@interface NetworkingManager ()

@property (nonatomic, strong) AFHTTPSessionManager *sessionManager;

@end

@implementation NetworkingManager

- (instancetype)init {
    self = [super init];
    if (self) {
        self.sessionManager = [AFHTTPSessionManager manager];
        self.sessionManager.requestSerializer = [AFJSONRequestSerializer serializer];
        self.sessionManager.requestSerializer.timeoutInterval = 15.0;
        self.sessionManager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"application/json",
                                                                         @"text/json", @"text/javascript",
                                                                         @"text/plain", nil];
    }
    return  self;
}

+ (NetworkingManager *)shareManager {
    static NetworkingManager *manager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        manager = [[NetworkingManager alloc] init];
    });
    return manager;
}

#pragma mark - User

+ (void)changeUserName:(NSString *)userName
            loginToken:(NSString *)loginToken
                 block:(void (^)(NetworkingResponse * _Nonnull))block {
    NSDictionary *content = @{@"user_name" : userName ?: @"",
                              @"login_token" : loginToken ?: @""};
    [self postWithEventName:@"changeUserName" space:@"login" content:content block:block];
}

#pragma mark -

+ (void)postWithEventName:(NSString *)eventName
                    space:(NSString *)space
                  content:(NSDictionary *)content
                    block:(void (^ __nullable)(NetworkingResponse *response))block {
    NSString *appid = [PublicParameterComponent share].appId;
    NSDictionary *parameters = @{@"event_name" : eventName ?: @"",
                                 @"content" : [content yy_modelToJSONString] ?: @{},
                                 @"device_id" : [NetworkingTool getDeviceId] ?: @"",
                                 @"app_id" : appid ? appid : @""};
    NSString *URLString = [NSString stringWithFormat:@"%@/%@",HeadUrl, space];
    [[self shareManager].sessionManager POST:URLString
                                  parameters:parameters
                                     headers:nil
                                    progress:nil
                                     success:^(NSURLSessionDataTask * _Nonnull task,
                                               id  _Nullable responseObject) {
        [self processResponse:responseObject block:block];
        NSLog(@"[%@]-%@ %@", [self class], eventName, responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        if (block) {
            NetworkingResponse *response = [[NetworkingResponse alloc] init];
            response.code = error.code;
            response.message = error.localizedDescription;
            block(response);
        }
        NSLog(@"[%@]-%@ failure %@", [self class], eventName, task.response);
    }];
}

+ (void)processResponse:(id _Nullable)responseObject
                  block:(void (^ __nullable)(NetworkingResponse *response))block {
    NetworkingResponse *response = [NetworkingResponse dataToResponseModel:responseObject];
    if (block) {
        block(response);
    }
    if (response.code == RTMStatusCodeTokenExpired) {
        [[NSNotificationCenter defaultCenter] postNotificationName:NotificationLoginExpired object:nil];
    }
}

@end
