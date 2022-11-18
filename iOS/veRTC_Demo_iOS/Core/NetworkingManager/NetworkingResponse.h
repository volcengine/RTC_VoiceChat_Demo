//
//  NetworkingResponse.h
//  veRTC_Demo
//
//  Created by on 2021/12/17.
//  
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NetworkingResponse : NSObject

@property (nonatomic, assign) NSInteger code;

@property (nonatomic, copy) NSString *message;

@property (nonatomic, assign) NSTimeInterval timestamp;

@property (nonatomic, assign, readonly) BOOL result;

@property (nonatomic, copy) NSDictionary *response;

+ (instancetype)dataToResponseModel:(id _Nullable)data;

+ (instancetype)badServerResponse;

@end

NS_ASSUME_NONNULL_END
