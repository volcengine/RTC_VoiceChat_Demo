//
//  MenuCellModel.h
//  veRTC_Demo
//
//  Created by on 2021/5/23.
//  
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface MenuCellModel : NSObject

@property (nonatomic, copy) NSString *title;

@property (nonatomic, copy) NSString *desTitle;

@property (nonatomic, assign) BOOL isMore;

@end

NS_ASSUME_NONNULL_END
