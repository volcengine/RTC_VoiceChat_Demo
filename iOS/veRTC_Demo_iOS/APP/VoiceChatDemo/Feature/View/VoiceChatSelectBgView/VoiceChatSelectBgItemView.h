//
//  VoiceChatSelectBgItemView.h
//  veRTC_Demo
//
//  Created by on 2021/11/26.
//  
//

#import "BaseButton.h"

NS_ASSUME_NONNULL_BEGIN

@interface VoiceChatSelectBgItemView : BaseButton

@property (nonatomic, assign) BOOL isSelected;

- (instancetype)initWithIndex:(NSInteger)index;

- (NSString *)getBackgroundImageName;

- (NSString *)getBackgroundSmallImageName;

@end

NS_ASSUME_NONNULL_END
