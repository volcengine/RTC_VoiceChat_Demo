// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "NetworkingManager+joinRTSParams.h"

@implementation NetworkingManager (joinRTSParams)

#pragma mark - SetAppInfo

+ (void)setAppInfoWithAppId:(NSDictionary *)dic
                      block:(void (^ __nullable)(NetworkingResponse *response))block {
    NSString *appId = [NSString stringWithFormat:@"%@", dic[@"appId"]];
    NSString *appKey = [NSString stringWithFormat:@"%@", dic[@"appKey"]];
    NSString *scenesName = [NSString stringWithFormat:@"%@", dic[@"scenesName"]];
    NSString *loginToken = [NSString stringWithFormat:@"%@", dic[@"loginToken"]];
    NSString *volcAk = dic[@"volcAk"];
    NSString *volcSk = dic[@"volcSk"];
    NSString *volcAccountID = dic[@"volcAccountID"];
    NSString *vodSpace = dic[@"vodSpace"];
    NSString *contentPartner = dic[@"contentPartner"];
    NSString *contentCategory = dic[@"contentCategory"];

    NSDictionary *content = @{@"app_id" : appId ?: @"",
                              @"app_key" : appKey ?: @"",
                              @"volc_ak" : volcAk ?: @"",
                              @"volc_sk" : volcSk ?: @"",
                              @"account_id" : volcAccountID ?: @"",
                              @"vod_space" : vodSpace ?: @"",
                              @"scenes_name" : scenesName ?: @"",
                              @"login_token" : loginToken ?: @"",
                              @"content_partner" : contentPartner ?: @"",
                              @"content_category" : contentCategory ?: @""};
    [self postWithEventName:@"setAppInfo" space:@"login" content:content block:block];
}

@end
