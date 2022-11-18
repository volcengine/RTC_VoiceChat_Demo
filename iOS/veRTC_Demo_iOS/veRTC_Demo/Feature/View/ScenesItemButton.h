//
//  ScenesItemButton.h
//  veRTC_Demo
//
//  Created by on 2021/5/26.
//  
//

#import <UIKit/UIKit.h>
#import "SceneButtonModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface ScenesItemButton : UIButton
@property (nonatomic, assign) SceneButtonModel *model;
- (void)addItemLayer;

@end

NS_ASSUME_NONNULL_END
