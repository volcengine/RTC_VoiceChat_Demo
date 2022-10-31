/*
 * Copyright 2022 CloudWeGo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package config

import (
	"fmt"
	"os"

	"github.com/jinzhu/configor"
)

const (
	DefaultConfDir  = "conf"
	DefaultConfFile = "config.yaml"
)

type Configuration struct {
	//general
	MysqlDSN      string `yaml:"mysql_dsn"`
	RedisAddr     string `yaml:"redis_addr"`
	RedisPassword string `yaml:"redis_password"`
	ServerUrl     string `yaml:"server_url"`
	Addr          string `yaml:"addr"`
	Port          string `yaml:"port"`

	VolcAk        string `yaml:"volc_ak"`
	VolcSk        string `yaml:"volc_sk"`
	VolcEngineUrl string `yaml:"volc_engine_url"`

	CheckLoginToken bool `yaml:"check_login_token"`

	//login
	AuditorPhoneCode map[string]string `yaml:"auditor_phone_code"`

	//chat solon
	CsAppID          string `yaml:"cs_app_id"`
	CsAppKey         string `yaml:"cs_app_key"`
	CsExperienceTime int    `yaml:"cs_experience_time"`

	//voice chat
	SvcAppID          string `yaml:"svc_app_id"`
	SvcAppKey         string `yaml:"svc_app_key"`
	SvcExperienceTime int    `yaml:"svc_experience_time"`

	//video interact
	ViAppID          string `yaml:"vi_app_id"`
	ViAppKey         string `yaml:"vi_app_key"`
	ViExperienceTime int    `yaml:"vi_experience_time"`

	//ktv
	KtvAppID          string `yaml:"ktv_app_id"`
	KtvAppKey         string `yaml:"ktv_app_key"`
	KtvExperienceTime int    `yaml:"ktv_experience_time"`

	//unknown
	RoomUserLimit    int `yaml:"room_user_limit"`
	ReconnectTimeout int `yaml:"reconnect_timeout"`
}

var configs *Configuration

// todo tcc
func InitConfig(file string) {
	configs = &Configuration{}
	if err := configor.Load(configs, file); err != nil {
		fmt.Fprintf(os.Stderr, "ParseConfig failed: err=%v\n", err)
		os.Exit(1)
	}
}

func Configs() *Configuration {
	if configs == nil {
		panic("config not init")
	}
	return configs
}
