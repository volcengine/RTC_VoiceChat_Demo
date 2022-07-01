//
//  NetworkingManager.m
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/16.
//  Copyright © 2021 bytedance. All rights reserved.
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
    [self postWithEventName:@"changeUserName" content:content block:block];
}

#pragma mark - SetAppInfo

+ (void)setAppInfoWithAppId:(NSString *)appId
                     appKey:(NSString *)appKey
                     volcAk:(NSString *)volcAk
                     volcSk:(NSString *)volcSk
              volcAccountID:(NSString *)volcAccountID
                   vodSpace:(NSString *)vodSpace
                      block:(void (^ __nullable)(NetworkingResponse *response))block {
    NSDictionary *content = @{@"app_id" : appId ?: @"",
                              @"app_key" : appKey ?: @"",
                              @"volc_ak" : volcAk ?: @"",
                              @"volc_sk" : volcSk ?: @"",
                              @"account_id" : volcAccountID ?: @"",
                              @"vod_space" : vodSpace ?: @""};
    [self postWithEventName:@"setAppInfo" content:content block:block];
}

#pragma mark - RTM

+ (void)joinRTM:(NSString *)scenes
     loginToken:(NSString *)loginToken
          block:(void (^)(NSString * _Nullable,
                          NSString * _Nullable,
                          NSString * _Nullable,
                          NSString * _Nullable,
                          NetworkingResponse * _Nonnull))block {
    NSDictionary *content = @{@"scenes_name" : scenes ?: @"",
                              @"login_token" : loginToken ?: @""};
    [self postWithEventName:@"joinRTM" content:content
                      block:^(NetworkingResponse *response) {
        NSString *appID = nil;
        NSString *RTMToken = nil;
        NSString *serverUrl = nil;
        NSString *serverSig = nil;
        if (response.result) {
            appID = response.response[@"app_id"];
            RTMToken = response.response[@"rtm_token"];
            serverUrl = response.response[@"server_url"];
            serverSig = response.response[@"server_signature"];
        }
        if (block) {
            block(appID, RTMToken, serverUrl, serverSig, response);
        }
    }];
}

#pragma mark -

+ (void)postWithEventName:(NSString *)eventName
                  content:(NSDictionary *)content
                    block:(void (^ __nullable)(NetworkingResponse *response))block {
    NSString *appid = [PublicParameterCompoments share].appId;
    NSDictionary *parameters = @{@"event_name" : eventName ?: @"",
                                 @"content" : [content yy_modelToJSONString] ?: @{},
                                 @"device_id" : [NetworkingTool getDeviceId] ?: @"",
                                 @"app_id" : appid ? appid : @""};
    [[self shareManager].sessionManager POST:LoginUrl
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
