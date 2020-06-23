package com.example.sound;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.sound.VoicePromptType.ABNORMAL_TEMPERATURE;
import static com.example.sound.VoicePromptType.NEAR_MEASURE;
import static com.example.sound.VoicePromptType.NORMAL_TEMPERATURE;
import static com.example.sound.VoicePromptType.NO_MASK;
import static com.example.sound.VoicePromptType.PASS;
import static com.example.sound.VoicePromptType.UNAUTHORIZED;

/**
 * 语音提示类型
 */
@IntDef({PASS, NORMAL_TEMPERATURE, ABNORMAL_TEMPERATURE, UNAUTHORIZED, NO_MASK, NEAR_MEASURE})
@Retention(RetentionPolicy.SOURCE)
public @interface VoicePromptType {
    int PASS = 0; // 请通过
    int NORMAL_TEMPERATURE = 1; // 温度正常
    int ABNORMAL_TEMPERATURE = 2; // 温度异常
    int UNAUTHORIZED = 3; // 未授权
    int NO_MASK = 4; // 未戴口罩
    int NEAR_MEASURE = 5; // 请靠近
}
