// 
// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT
// 

#import "FeedbackManagerProtocol.h"

@interface FeedbackManagerProtocol ()

@property (nonatomic, strong) id<FeedbackManagerDelegate> feedbackDelegate;

@end

@implementation FeedbackManagerProtocol

- (instancetype)initWithSuperView:(UIView *)superView
                        scenesDic:(nonnull NSDictionary *)scenesDic {
    NSObject<FeedbackManagerDelegate> *feedback = [[NSClassFromString(@"FeedbackHome") alloc] init];
    if (feedback) {
        self.feedbackDelegate = feedback;
    }
    
    if ([self.feedbackDelegate respondsToSelector:@selector(protocol:initWithSuperView:scenesDic:)]) {
        return [self.feedbackDelegate protocol:self
                             initWithSuperView:superView
                                     scenesDic:scenesDic];
    } else {
        return nil;
    }
}

@end
