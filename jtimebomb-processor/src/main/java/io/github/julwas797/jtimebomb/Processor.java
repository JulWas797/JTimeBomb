package io.github.julwas797.jtimebomb;

import io.github.julwas797.jtimebomb.visitor.TimeBombClassVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class Processor {
    public static void main(String[] args) {
        if (args.length != 2) {
            log.error("Not enough arguments. Usage: java -jar jtimebomb-processor.jar <INPUT PATH> <OUTPUT PATH>");
        }

        new Processor(args[0], args[1]).process();
    }

    private static final Logger log = LogManager.getLogger(Processor.class);

    private final String inputPath;
    private final String outputPath;

    protected Processor(String inputPath, String outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public void process() {
        log.info("Processing, output='{}' input='{}'", inputPath, outputPath);

        try (var jar = new JarFile(inputPath);
             var jos = new JarOutputStream(Files.newOutputStream(Path.of(outputPath)))) {
            var entries = jar.entries();

            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();

                log.info("Processing entry '{}'", entry.getName());
                var start = System.currentTimeMillis();

                if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                    log.info("Skipping entry");
                    jos.putNextEntry(entry);
                    jos.closeEntry();
                    continue;
                }

                try (var in = jar.getInputStream(entry)) {
                    var cr = new ClassReader(in.readAllBytes());
                    var cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

                    cr.accept(new TimeBombClassVisitor(Opcodes.ASM8, cw), 0);

                    jos.putNextEntry(entry);
                    jos.write(cw.toByteArray());
                    jos.closeEntry();

                    var elapsed = System.currentTimeMillis() - start;

                    log.info("Processed entry '{}' in {}ms", entry.getName(), elapsed);
                }
            }

        } catch (IOException e) {
            log.error(e);
        }
    }
}