//
//  JoinRTSParams.h
//  veRTC_Demo
//
//  Created by bytedance on 2021/12/23.
//  Copyright © 2021 bytedance. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JoinRTSParamsModel.h"
#import "JoinRTSInputModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface JoinRTSParams : NSObject

/*
 * Get RTS login information
 * @param Input data model
 * @param block callback
 */
+ (void)getJoinRTSParams:(JoinRTSInputModel *)inputModel
                   block:(void (^)(JoinRTSParamsModel *model))block;
                          
/*
* Network request public parameter usage
* @param dic Dic parameter, can be nil
*/
+ (NSDictionary *)addTokenToParams:(NSDictionary * _Nullable)dic;

@end

NS_ASSUME_NONNULL_END
