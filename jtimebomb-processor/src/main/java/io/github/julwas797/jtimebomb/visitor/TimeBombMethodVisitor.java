package io.github.julwas797.jtimebomb.visitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.objectweb.asm.Opcodes.*;

public class TimeBombMethodVisitor extends MethodVisitor {
    private static final Logger log = LogManager.getLogger(TimeBombMethodVisitor.class);

    private boolean process = false;
    private String message;
    private LocalDateTime time;
    private String timeMethod;

    protected TimeBombMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals("Lio/github/julwas797/jtimebomb/annotations/TimeBomb;")) {
            process = true;

            log.info("Found TimeBomb annotation!");

            return new AnnotationVisitor(api) {
                @Override
                public void visit(String name, Object value) {
                    if (value instanceof String sValue) {
                        switch (name) {
                            case "value":
                                log.info("With value: {}", sValue);
                                try {
                                    time = LocalDateTime.parse(sValue);
                                } catch (DateTimeParseException e) {
                                    log.error(e.getMessage());
                                }
                                break;
                            case "message":
                                log.info("With message: {}", sValue);
                                message = sValue;
                                break;
                            case "timeMethod":
                                log.info("With timeMethod: {}", sValue);
                                timeMethod = sValue;
                                break;
                            default:
                                log.warn("Unknown option found in the annotation: {}", name);
                                break;
                        }
                    }
                }
            };
        }

        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitCode() {
        super.visitCode();

        if (!process) {
            return;
        }

        if (time == null) {
            log.error("Missing value field!");
            throw new IllegalStateException("Value field has not been set!");
        }

        // Load time to stack

        var year = time.getYear();
        var month = time.getMonthValue();
        var day = time.getDayOfMonth();
        var hour = time.getHour();
        var minute = time.getMinute();
        var second = time.getSecond();
        var nano = time.getNano();

        mv.visitIntInsn(SIPUSH, year);
        mv.visitIntInsn(BIPUSH, month);
        mv.visitIntInsn(BIPUSH, day);
        mv.visitIntInsn(BIPUSH, hour);
        mv.visitIntInsn(BIPUSH, minute);
        mv.visitIntInsn(BIPUSH, second);
        mv.visitIntInsn(SIPUSH, nano);

        mv.visitMethodInsn(
                INVOKESTATIC,
                "java/time/LocalDateTime",
                "of",
                "(IIIIIII)Ljava/time/LocalDateTime;",
                false
        );

        mv.visitVarInsn(ASTORE, 0);
        mv.visitVarInsn(ALOAD, 0);

        var nowClass = "java/time/LocalDateTime";
        var nowMethod = "now";

        if (timeMethod != null && !timeMethod.isEmpty()) {
            var split = timeMethod.split(":");
            nowClass = split[0].replace(".", "/");
            nowMethod = split[1];
        }

        mv.visitMethodInsn(
                INVOKESTATIC,
                nowMethod,
                nowClass,
                "()Ljava/time/LocalDateTime;",
                false
        );

        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/time/LocalDateTime",
                "isBefore",
                "(Ljava/time/chrono/ChronoLocalDateTime;)Z",
                false
        );

        var exitLabel = new Label();
        var continueLabel = new Label();

        mv.visitJumpInsn(IFEQ, continueLabel);

        mv.visitLabel(exitLabel);

        // Print message if present
        if (message != null && !message.isEmpty()) {
            mv.visitFieldInsn(
                    GETSTATIC,
                    "java/lang/System",
                    "out",
                    "Ljava/io/PrintStream;"
            );

            mv.visitLdcInsn(message);

            mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/io/PrintStream",
                    "println",
                    "(Ljava/lang/String;)V",
                    false
            );
        }

        mv.visitInsn(ICONST_0);

        mv.visitMethodInsn(
                INVOKESTATIC,
                "java/lang/System",
                "exit",
                "(I)V",
                false
        );

        mv.visitLabel(continueLabel);

        log.info("Added instructions");
    }
}
