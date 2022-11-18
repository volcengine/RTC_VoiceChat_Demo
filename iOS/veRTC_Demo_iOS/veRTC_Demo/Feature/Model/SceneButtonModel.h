//
//  SceneCellModel.h
//  veRTC_Demo
//
//  Created by on 2021/7/26.
//  
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface SceneButtonModel : NSObject
@property (nonatomic, copy) NSString *iconName;
@property (nonatomic, copy) NSString *bgName;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, strong) NSObject *scenes;

@end

NS_ASSUME_NONNULL_END
