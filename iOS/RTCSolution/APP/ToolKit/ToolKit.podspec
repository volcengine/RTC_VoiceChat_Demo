
Pod::Spec.new do |spec|
  spec.name         = 'ToolKit'
  spec.version      = '1.0.0'
  spec.summary      = 'Core'
  spec.description  = 'Core ...'
  spec.homepage     = 'https://github.com/volcengine'
  spec.license      = { :type => 'MIT', :file => 'LICENSE' }
  spec.author       = { 'author' => 'volcengine rtc' }
  spec.source       = { :path => './'}
  spec.ios.deployment_target = '9.0'
  
  spec.prefix_header_contents = '#import "Constants.h"'
  spec.resource_bundles = {
    'ToolKit' => ['Resource/*.{xcassets,bundle}']
  }
  spec.pod_target_xcconfig = {'CODE_SIGN_IDENTITY' => ''}
  spec.source_files = '**/*.{h,m}'
  spec.dependency 'Masonry'
  spec.dependency 'YYModel'
  spec.dependency 'AFNetworking'

  spec.dependency 'VolcEngineRTC'
  
  
end
