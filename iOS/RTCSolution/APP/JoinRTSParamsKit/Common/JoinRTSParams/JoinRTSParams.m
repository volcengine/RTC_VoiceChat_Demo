// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "JoinRTSParams.h"
#import "NetworkingManager+joinRTSParams.h"
#import "JoinRTSConfig.h"

@implementation JoinRTSParams

+ (void)getJoinRTSParams:(JoinRTSInputModel *)inputModel
                   block:(void (^)(JoinRTSParamsModel *model))block {
    NSString *errorMessage = @"";
    NSMutableDictionary *dic = [[NSMutableDictionary alloc] init];
    if (NOEmptyStr(APPID)) {
        [dic setValue:APPID forKey:@"appId"];
    } else {
        errorMessage = @"APPID";
    }
    
    if (NOEmptyStr(APPKey)) {
        [dic setValue:APPKey forKey:@"appKey"];
    } else {
        errorMessage = @"APPKey";
    }
    
    if (NOEmptyStr(AccessKeyID)) {
        [dic setValue:AccessKeyID forKey:@"volcAk"];
    } else {
        errorMessage = @"AccessKeyID";
    }
    
    if (NOEmptyStr(SecretAccessKey)) {
        [dic setValue:SecretAccessKey forKey:@"volcSk"];
    } else {
        errorMessage = @"SecretAccessKey";
    }
    
    if (NOEmptyStr(inputModel.scenesName)) {
        [dic setValue:inputModel.scenesName forKey:@"scenesName"];
    } else {
        errorMessage = @"scenes";
    }
    
    if (NOEmptyStr(inputModel.loginToken)) {
        [dic setValue:inputModel.loginToken forKey:@"loginToken"];
    } else {
        errorMessage = @"loginToken";
    }
    if (NOEmptyStr(errorMessage)) {
        errorMessage = [NSString stringWithFormat:@"%@ 为空请查看配置", errorMessage];
        if (block) {
            block(nil);
        }
        return;
    }
    if (NOEmptyStr(inputModel.volcAccountID)) {
        [dic setValue:inputModel.volcAccountID forKey:@"volcAccountID"];
    }
    if (NOEmptyStr(inputModel.vodSpace)) {
        [dic setValue:inputModel.vodSpace forKey:@"vodSpace"];
    }
    if (NOEmptyStr(inputModel.contentPartner)) {
        [dic setValue:inputModel.contentPartner forKey:@"contentPartner"];
    }
    if (NOEmptyStr(inputModel.contentCategory)) {
        [dic setValue:inputModel.contentCategory forKey:@"contentCategory"];
    }
    [PublicParameterComponent share].appId = APPID;
    [NetworkingManager setAppInfoWithAppId:dic
                                     block:^(NetworkingResponse * _Nonnull response) {
        if (response.result) {
            JoinRTSParamsModel *paramsModel = [JoinRTSParamsModel yy_modelWithJSON:response.response];
            if (block) {
                block(paramsModel);
            }
        } else {
            if (block) {
                block(nil);
            }
        }
    }];
}
                          
+ (NSDictionary *)addTokenToParams:(NSDictionary *)dic {
    NSMutableDictionary *tokenDic = nil;
    if (dic && [dic isKindOfClass:[NSDictionary class]] && dic.count > 0) {
        tokenDic = [dic mutableCopy];
    } else {
        tokenDic = [[NSMutableDictionary alloc] init];
    }
    if (NOEmptyStr([LocalUserComponent userModel].uid)) {
        [tokenDic setValue:[LocalUserComponent userModel].uid
                    forKey:@"user_id"];
    }
    if (NOEmptyStr([LocalUserComponent userModel].loginToken)) {
        [tokenDic setValue:[LocalUserComponent userModel].loginToken
                    forKey:@"login_token"];
    }
    if (NOEmptyStr([PublicParameterComponent share].appId)) {
        [tokenDic setValue:[PublicParameterComponent share].appId
                    forKey:@"app_id"];
    }
    if (NOEmptyStr([PublicParameterComponent share].roomId)) {
        [tokenDic setValue:[PublicParameterComponent share].roomId
                    forKey:@"room_id"];
    }
    
    return [tokenDic copy];
}

@end
