//
//  VoiceChatAvatarView.m
//  SceneRTCDemo
//
//  Created by on 2021/3/10.
//

#import "VoiceChatAvatarView.h"

@interface VoiceChatAvatarView ()

@property (nonatomic, strong) UILabel *avatarLabel;
@property (nonatomic, strong) UIImageView *bgImageImageView;

@end

@implementation VoiceChatAvatarView

- (instancetype)init {
    self = [super init];
    if (self) {
        [self addSubview:self.bgImageImageView];
        [self.bgImageImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self);
        }];
        
        [self addSubview:self.avatarLabel];
        [self.avatarLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.center.equalTo(self.bgImageImageView);
        }];
    }
    return self;
}

- (void)setFontSize:(NSInteger)fontSize {
    _fontSize = fontSize;
    
    self.avatarLabel.font = [UIFont systemFontOfSize:fontSize weight:UIFontWeightMedium];
}

- (void)setText:(NSString *)text {
    _text = text;
    
    if (NOEmptyStr(text)) {
        if (text.length > 0) {
            self.avatarLabel.text = [text substringToIndex:1];
        }
    }
}

#pragma mark - getter

- (UIImageView *)bgImageImageView {
    if (!_bgImageImageView) {
        _bgImageImageView = [[UIImageView alloc] init];
        _bgImageImageView.image = [UIImage imageNamed:@"voicechat_small_bg" bundleName:HomeBundleName];
    }
    return _bgImageImageView;
}

- (UILabel *)avatarLabel {
    if (!_avatarLabel) {
        _avatarLabel = [[UILabel alloc] init];
        _avatarLabel.textColor = [UIColor colorFromHexString:@"#FFFFFF"];
        
    }
    return _avatarLabel;
}

@end
