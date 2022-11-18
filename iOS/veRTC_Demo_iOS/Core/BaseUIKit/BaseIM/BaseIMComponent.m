//
//  BaseIMComponent.m
//  veRTC_Demo
//
//  Created by on 2021/5/23.
//  
//

#import "BaseIMComponent.h"
#import "BaseIMView.h"
#import "Masonry.h"
#import "DeviceInforTool.h"

@interface BaseIMComponent ()

@property (nonatomic, strong) BaseIMView *BaseIMView;

@end

@implementation BaseIMComponent

- (instancetype)initWithSuperView:(UIView *)superView {
    self = [super init];
    if (self) {
        [superView addSubview:self.BaseIMView];
        [self.BaseIMView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(20);
            make.bottom.mas_equalTo(-10 - ([DeviceInforTool getVirtualHomeHeight] + 64));
            make.height.mas_equalTo(115);
            make.width.mas_equalTo(275);
        }];
    }
    return self;
}

#pragma mark - Publish Action

- (void)addIM:(BaseIMModel *)model {
    NSMutableArray *datas = [[NSMutableArray alloc] initWithArray:self.BaseIMView.dataLists];
    [datas addObject:model];
    self.BaseIMView.dataLists = [datas copy];
}

#pragma mark - getter

- (BaseIMView *)BaseIMView {
    if (!_BaseIMView) {
        _BaseIMView = [[BaseIMView alloc] init];
    }
    return _BaseIMView;
}

@end
