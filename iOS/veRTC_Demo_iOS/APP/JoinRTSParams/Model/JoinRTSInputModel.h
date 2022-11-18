//
//  JoinRTSInputModel.h
//  JoinRTSParams
//
//  Created by bytedance on 2022/7/15.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface JoinRTSInputModel : NSObject

@property (nonatomic, copy) NSString *scenesName;

@property (nonatomic, copy) NSString *loginToken;

@property (nonatomic, copy) NSString *volcAccountID;

@property (nonatomic, copy) NSString *vodSpace;

@property (nonatomic, copy) NSString *contentPartner;

@property (nonatomic, copy) NSString *contentCategory;

@end

NS_ASSUME_NONNULL_END
