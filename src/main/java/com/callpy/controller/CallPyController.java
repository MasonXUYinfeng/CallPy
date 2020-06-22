package com.callpy.controller;

import com.alibaba.fastjson.JSON;
import com.callpy.core.Result;
import com.callpy.core.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/executescript")
public class CallPyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallPyController.class);

    @PostMapping("/execpython")
    public String execPython(@RequestBody Map<String, Object> reqMap) {
        Result result = ResultGenerator.genSuccessResult();

        String executionFilePath = (String)reqMap.get("executionFilePath");
        File file = new File(executionFilePath);

        if (file.exists()) {
            LOGGER.info("可以读取到非项目中脚本");
        } else {
            LOGGER.info("不可以读取到非项目中脚本");
            result = ResultGenerator.genFailResult("不可以读取到非项目中脚本");
        }

        try {
            reqMap.remove("executionFilePath");
            String parameter = JSON.toJSONString(reqMap);
            String parameters = parameter.replaceAll("[\"']", "\\\\$0");
            LOGGER.debug("parameters==" + parameters);
            // 定义传入Python脚本的命令行参数，将参数放入字符串数组里
            String cmds = String.format("python %s %s", executionFilePath, parameters);
            LOGGER.debug("cmds==" + cmds);
            Process pcs = Runtime.getRuntime().exec(cmds);
            LOGGER.debug("执行完Runtime.getRuntime().exec");

            // 定义Python脚本的返回值
            String response = null;
            // 获取CMD的返回流
            BufferedInputStream in = new BufferedInputStream(pcs.getInputStream());
            // 字符流转换字节流
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            // 这里也可以输出文本日志

            String lineStr = null;
            while ((lineStr = br.readLine()) != null) {
                response = lineStr;
            }
            // 关闭输入流
            br.close();
            in.close();
            LOGGER.debug("执行结果：" + response);

            int re = pcs.waitFor();
            LOGGER.debug("python执行完毕=" + re);

        } catch (Exception e) {
            result = ResultGenerator.genFailResult("执行python脚本失败");
        }

        return result.toString();
    }
}
