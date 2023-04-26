
Pod::Spec.new do |spec|
  spec.name         = 'JoinRTSParamsKit'
  spec.version      = '1.0.0'
  spec.summary      = 'JoinRTSParamsKit APP'
  spec.description  = 'JoinRTSParamsKit App ..'
  spec.homepage     = 'https://github.com/volcengine'
  spec.license      = { :type => 'MIT', :file => 'LICENSE' }
  spec.author       = { 'author' => 'volcengine rtc' }
  spec.source       = { :path => './'}
  spec.ios.deployment_target = '9.0'
  
  spec.pod_target_xcconfig = {'CODE_SIGN_IDENTITY' => ''}
  spec.source_files = '**/*.{h,m,c,mm}'
  spec.prefix_header_contents = '#import "ToolKit.h"'
  spec.dependency 'ToolKit'
  spec.dependency 'YYModel'
end
