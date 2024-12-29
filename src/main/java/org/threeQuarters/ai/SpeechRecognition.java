package org.threeQuarters.ai;

import java.io.File;
import java.io.IOException;

/**
 * Transcribes speeches into text.
 */
public class SpeechRecognition {
    private String exePath;
    private String resourcePath;

    public SpeechRecognition() {
        resourcePath = getClass().getClassLoader().getResource("").getPath().substring(1);
        exePath = resourcePath + "summary/summary.exe";
    }

    /**
     * Transcribes speeches into text.
     * Example: transcribe(ASRModelType.TINY, "test.mp4", "result.txt")
     * 
     * @param model  The model used by ASR (also see ASRModelType)
     * @param source The source file, video or audio (relative to resource or
     *               absolute)
     * @param target The target file of text (relative to resource or absolute)
     * @return Exit code of the ASR process, 0 for success
     * @throws IOException
     * @throws InterruptedException
     * @throws IllegalArgumentException
     */
    public int transcribe(ASRModelType model, String source, String target)
            throws IOException, InterruptedException, IllegalArgumentException {
        File sourceFile = new File(source);
        if (!sourceFile.isAbsolute()) {
            source = resourcePath + source;
            sourceFile = new File(source);
        }
        if (!sourceFile.isFile()) {
            throw new IllegalArgumentException("Source file illegal.");
        }
        File targetFile = new File(target);
        if (!targetFile.isAbsolute()) {
            target = resourcePath + target;
            targetFile = new File(target);
        }
        if (!targetFile.isAbsolute()) {
            throw new IllegalArgumentException("Target file illegal.");
        }
        System.out.println("processing" + source);
        String[] cmd = { exePath, model.toString(), source, target };
        Process process = Runtime.getRuntime().exec(cmd);
        int exitCode = process.waitFor();
        return exitCode;
    }

    public static void main(String[] args) {
        SpeechRecognition speechRecognition = new SpeechRecognition();
        try {
            int exitCode = speechRecognition.transcribe(ASRModelType.TINY, "test.mp4", "result.txt");
            System.err.println("Transcription exits " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
