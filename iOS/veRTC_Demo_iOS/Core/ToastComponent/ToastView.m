//
//  ToastView.m
//  veRTC_Demo
//
//  Created by on 2021/6/3.
//
//

#import "ToastView.h"
#import "Masonry.h"

@interface ToastView ()

@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UIActivityIndicatorView *activityIndicatorView;

@end


@implementation ToastView

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.bgView = [[UIView alloc] init];
        self.bgView.backgroundColor = [UIColor blackColor];
        self.bgView.alpha = 0.8;
        self.bgView.layer.cornerRadius = 4;
        self.bgView.layer.masksToBounds = YES;
        [self addSubview:self.bgView];
        
        [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.center.equalTo(self);
        }];
    }
    return self;
}

- (void)setContentType:(ToastViewContentType)contentType {
    if (_contentType == contentType) {
        return;
    }
    _contentType = contentType;
    while (self.bgView.subviews.count) {
        [self.bgView.subviews.lastObject removeFromSuperview];
    }
    switch (contentType) {
        case ToastViewContentTypeMeesage:
            [self configMeesageContent];
            break;
        case ToastViewContentTypeActivityIndicator:
            [self configActivityIndicatorContent];
            break;
        default:
            break;
    }
}

- (void)setMeesage:(NSString *)message {
    if (self.contentType != ToastViewContentTypeMeesage) {
        self.contentType = ToastViewContentTypeMeesage;
    }
    self.titleLabel.text = message;
}

- (void)startLoading {
    if (self.contentType != ToastViewContentTypeActivityIndicator) {
        self.contentType = ToastViewContentTypeActivityIndicator;
    }
    [self.activityIndicatorView startAnimating];
}

- (void)stopLoaidng {
    [self.activityIndicatorView stopAnimating];
}

- (void)configMeesageContent {
    CGFloat minScreen = MIN([UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height);
    CGFloat scale = (minScreen / 375);
    UILabel *titleLabel = [[UILabel alloc] init];
    titleLabel.numberOfLines = 0;
    titleLabel.textColor = [UIColor whiteColor];
    titleLabel.textAlignment = NSTextAlignmentCenter;
    titleLabel.font = [UIFont systemFontOfSize:16.0 * scale weight:UIFontWeightRegular];
    [self.bgView addSubview:titleLabel];
    self.titleLabel = titleLabel;
    
    [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.equalTo(self.bgView);
        make.width.mas_lessThanOrEqualTo(minScreen - 24 * 2);
    }];
    
    [self.bgView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(titleLabel).offset(-12);
        make.right.equalTo(titleLabel).offset(12);
        make.top.equalTo(titleLabel).offset(-12);;
        make.bottom.equalTo(titleLabel).offset(12);;
    }];
}

- (void)configActivityIndicatorContent {
    UIActivityIndicatorView *activityIndicatorView = nil;
    if (@available(iOS 13.0, *)) {
        activityIndicatorView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleLarge];
        activityIndicatorView.color = [UIColor whiteColor];
    } else {
        activityIndicatorView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    }
    [self.bgView addSubview:activityIndicatorView];
    self.activityIndicatorView = activityIndicatorView;
    
    [activityIndicatorView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.equalTo(self.bgView);
    }];
    
    [self.bgView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(activityIndicatorView).offset(-12);
        make.right.equalTo(activityIndicatorView).offset(12);
        make.top.equalTo(activityIndicatorView).offset(-12);;
        make.bottom.equalTo(activityIndicatorView).offset(12);;
    }];
}

@end
