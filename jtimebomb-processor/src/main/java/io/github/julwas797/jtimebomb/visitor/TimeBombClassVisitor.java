package io.github.julwas797.jtimebomb.visitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class TimeBombClassVisitor extends ClassVisitor {

    private static final Logger log = LogManager.getLogger(TimeBombClassVisitor.class);

    public TimeBombClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        log.info("Checking method {}", name);
        var mv = super.visitMethod(access, name, desc, signature, exceptions);
        return new TimeBombMethodVisitor(api, mv);
    }
}
