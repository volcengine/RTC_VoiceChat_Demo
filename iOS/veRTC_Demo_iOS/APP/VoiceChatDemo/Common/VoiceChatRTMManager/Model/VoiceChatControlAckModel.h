//
//  VoiceChatControlAckModel.h
//  SceneRTCDemo
//
//  Created by bytedance on 2021/3/17.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatControlAckModel : NSObject

@property (nonatomic, assign) NSInteger code;

@property (nonatomic, copy) NSString *message;

@property (nonatomic, assign) BOOL result;

@property (nonatomic, strong) id response;

@end

NS_ASSUME_NONNULL_END
