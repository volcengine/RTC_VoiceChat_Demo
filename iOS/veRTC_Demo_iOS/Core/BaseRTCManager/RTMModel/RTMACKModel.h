//
//  RTMACKModel.h
//  veRTC_Demo
//
//  Created by on 2021/12/21.
//  
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface RTMACKModel : NSObject

@property (nonatomic, copy) NSString *requestID;
@property (nonatomic, assign) NSInteger code;
@property (nonatomic, copy) NSString *message;
@property (nonatomic, copy) NSString *timestamp;
@property (nonatomic, assign, readonly) BOOL result;
@property (nonatomic, strong) id response;

+ (instancetype)modelWithMessageData:(id)data;

@end

NS_ASSUME_NONNULL_END
