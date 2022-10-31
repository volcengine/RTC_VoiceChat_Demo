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
package main

import (
	"flag"
	"fmt"
	"math/rand"
	"time"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/cmd/handler"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/cmd/api"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/config"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/db"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/redis_cli"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/task"
)

var conf = flag.String("config", "conf/config.yaml", "server config file path")

func main() {
	fmt.Println("start")
	rand.Seed(time.Now().UnixNano())

	Init()

	h := handler.NewEventHandlerDispatch()
	//start http api
	r := api.NewHttpApi(h)
	err := r.Run()
	if err != nil {
		panic("http server start failed,error:" + err.Error())
	}
}

func Init() {
	//get config
	config.InitConfig(*conf)

	logs.InitLog()

	//init db and redis
	db.Open(config.Configs().MysqlDSN)
	redis_cli.NewRedis(config.Configs().RedisAddr, config.Configs().RedisPassword)

	c := task.GetCronTask()
	c.Start()
}
