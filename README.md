# sound
在我们日常生活中,一些常用的场景例如银行操作机，人脸识别通行机等，每次进行一次操作，则机器回应一点声音。像人脸识别这种响应声音可能会更频繁一点。

如果您适应以下情景，那么您可以继续往下看了：
1. 需要多种不同类型声音排队播放
2. 需要支持中断队列某种类型声音，支持立即中断或者播放完当前语音才中断
3. 需要某种类型声音一直播放
4. 需要使用SoundPool这种反应比较快的，并且能根据时长结束进行下一步动作

        // 初始化几种类型语音
        MediaPlayerQueue mediaPlayerQueue = new MediaPlayerQueue();
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.PASS, loadSound("pass.mp3"));
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.UNAUTHORIZED, loadSound("no_auth.mp3"));
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.NO_MASK, loadSound("no_mask.mp3"));
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.NORMAL_TEMPERATURE, loadSound("temp_normal.mp3"));
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.ABNORMAL_TEMPERATURE, loadSound("temp_exception.mp3"));
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.NEAR_MEASURE, loadSound("near_measure_temperature.mp3"));

        // 设置请靠近测温允许重复播放
        ArrayList<Integer> repetitions = new ArrayList<>();
        repetitions.add(VoicePromptType.NEAR_MEASURE);
        mediaPlayerQueue.setRepetitions(repetitions);

        // 播放请靠近测温5次
        findViewById(R.id.button1).setOnClickListener(v -> {
            for (int i = 0; i < 5; i++) {
                mediaPlayerQueue.play(VoicePromptType.NEAR_MEASURE);
            }

        });

        // 播放温度正常，播放pass
        findViewById(R.id.button2).setOnClickListener(v -> {
            mediaPlayerQueue.play(VoicePromptType.NORMAL_TEMPERATURE);
            mediaPlayerQueue.play(VoicePromptType.PASS);
        });

        // 立即停止所有
        findViewById(R.id.button3).setOnClickListener(v -> mediaPlayerQueue.clearAll(true));

        // 停止所有（除了当前正在播放的）
        findViewById(R.id.button4).setOnClickListener(v -> mediaPlayerQueue.clearAll(false));

        // 立即停止所有 靠近测温的
        findViewById(R.id.button5).setOnClickListener(v -> mediaPlayerQueue.clearAllSpecifyType(VoicePromptType.NEAR_MEASURE));
