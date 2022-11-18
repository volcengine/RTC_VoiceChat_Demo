//
//  VoiceChatSelectBgView.m
//  veRTC_Demo
//
//  Created by on 2021/11/26.
//  
//

#import "VoiceChatSelectBgView.h"
#import "VoiceChatSelectBgItemView.h"

@interface VoiceChatSelectBgView ()

@property (nonatomic, strong) NSMutableArray *list;

@end

@implementation VoiceChatSelectBgView

- (instancetype)init {
    self = [super init];
    if (self) {
        for (int i = 0; i < 3; i++) {
            VoiceChatSelectBgItemView *itemView = [[VoiceChatSelectBgItemView alloc] initWithIndex:i];
            [itemView addTarget:self action:@selector(itemViewAction:) forControlEvents:UIControlEventTouchUpInside];
            [self addSubview:itemView];
            [self.list addObject:itemView];
        }
        [self.list mas_distributeViewsAlongAxis:MASAxisTypeHorizontal withFixedSpacing:12 leadSpacing:0 tailSpacing:0];
        [self.list mas_updateConstraints:^(MASConstraintMaker *make) {
            make.top.bottom.equalTo(self);
        }];
        
        VoiceChatSelectBgItemView *itemView = self.list.firstObject;
        itemView.isSelected = YES;
    }
    return self;
}

- (NSString *)getDefaults {
    VoiceChatSelectBgItemView *itemView = self.list.firstObject;
    return [itemView getBackgroundImageName];
}

- (void)itemViewAction:(VoiceChatSelectBgItemView *)itemView {
    for (VoiceChatSelectBgItemView *item in self.list) {
        item.isSelected = NO;
    }
    itemView.isSelected = YES;
    if (self.clickBlock) {
        self.clickBlock([itemView getBackgroundImageName],
                        [itemView getBackgroundSmallImageName]);
    }
}

#pragma mark - Getter

- (NSMutableArray *)list {
    if (!_list) {
        _list = [[NSMutableArray alloc] init];
    }
    return _list;
}

@end
