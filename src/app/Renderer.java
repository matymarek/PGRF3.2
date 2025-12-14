package app;

import app.solid.*;
import lwjglutils.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import transforms.*;

import java.io.IOException;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;


public class Renderer extends AbstractRenderer {

    private Solid triangle, axis, grid, quad, gridstrip;
    private int shaderProgramTriangle, shaderProgramAxis, shaderProgramGrid, shaderProgramQuad, shaderProgramAO, shaderProgramAOBlur;
    private Mat4 proj, grid1, grid2, lightModel;
    private OGLRenderTarget renderTarget, aoTarget, aoBlurTarget;
    private boolean usePerspective = true;
    private final double fov = Math.toRadians(70);
    double modelScale = 1.0;
    double modelRotationY = 0.0;
    private float time = 0;
    private int surfaceId = 0;

    // Camera
    private Camera camera;
    private boolean keyW, keyS, keyA, keyD, keyQ, keyE;
    private double lastMouseX, lastMouseY;
    private boolean rightMouseDown = false;
    private boolean firstMouse = true;

    // Textures
    private OGLTexture2D textureBricks, textureGlobe;

    // Light
    private Vec3D lightPos = new Vec3D(3, 3, 3);
    private Vec3D lightDir;

    // Blinn-Phong
    private boolean enableAmbient = true;
    private boolean enableDiffuse = true;
    private boolean enableSpecular = true;
    private boolean enableSpot = false;
    private boolean enableAttenuation = false;

    public void init() {
        this.height = LwjglWindow.HEIGHT;
        this.width = LwjglWindow.WIDTH;
        glClearColor(0.1f, 0.1f, 0.1f, 2.0f);
        glEnable(GL_DEPTH_TEST);

        camera = new Camera()
                .withPosition(new Vec3D(0.5, 8, 2.5))
                .withAzimuth(Math.toRadians(-95))
                .withZenith(Math.toRadians(-13.5))
                .withFirstPerson(true);

        updateProjection();
        updateLightModelAndDir();

        grid1 = new Mat4Transl(-2.0, 0.0, 0.0);
        grid2 = new Mat4Transl( 2.0, 0.0, 0.0);

        triangle = new Triangle();
        axis = new Axis();
        grid = new Grid(100, 100);
        gridstrip = new GridStrip(100,100);
        quad = new Grid(2, 2);

        // Trojuhelnik
        shaderProgramTriangle = ShaderUtils.loadProgram("/triangle");
        // Osy
        shaderProgramAxis = ShaderUtils.loadProgram("/axis");
        //Quad
        shaderProgramQuad = ShaderUtils.loadProgram("/quad");
        // Grid
        shaderProgramGrid = ShaderUtils.loadProgram("/grid");
        // AO
        shaderProgramAO = ShaderUtils.loadProgram("/ao");
        // Blur
        shaderProgramAOBlur = ShaderUtils.loadProgram("/aoBlur");
        // Textures
        try {
            textureBricks = new OGLTexture2D("textures/bricks.jpg");
            textureGlobe = new OGLTexture2D("textures/globe.jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        renderTarget = new OGLRenderTarget(width, height);
        aoTarget = new OGLRenderTarget(width, height);
        aoBlurTarget = new OGLRenderTarget(width, height);
    }
    public void display() {
        updateCameraMovement();
        drawFirst();
        drawAO();
        drawAOBlur();
        drawSecond();
    }
    private void drawFirst() {
        renderTarget.bind();
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        time += 0.01f;

        // Transformace
        Mat4 scale = new Mat4Scale(modelScale, modelScale, modelScale);
        Mat4 rot  = new Mat4RotY(modelRotationY);
        Mat4 transl = new Mat4Transl(2.0, 0.0, 0.0);
        grid2 = transl.mul(rot).mul(scale);

        // --- Vykresleni ---

        // Trojúhelník
        glUseProgram(shaderProgramTriangle);
        setGlobalUniforms(shaderProgramTriangle);
        int locModel = glGetUniformLocation(shaderProgramTriangle, "uModel");
        glUniformMatrix4fv(locModel, false, lightModel.floatArray());
        int locColor = glGetUniformLocation(shaderProgramTriangle, "uColor");
        glUniform3f(locColor, 1.0f, 1.0f, 0.2f);
        triangle.getBuffers().draw(GL_TRIANGLES, shaderProgramTriangle);

        // Osy
        glUseProgram(shaderProgramAxis);
        setGlobalUniforms(shaderProgramAxis);
        axis.getBuffers().draw(GL_LINES, shaderProgramAxis);

        //Gridy
        glUseProgram(shaderProgramGrid);
        setGlobalUniforms(shaderProgramGrid);
        setLightUniforms(shaderProgramGrid);
        int locUTime = glGetUniformLocation(shaderProgramGrid, "uTime");
        int locUSurfaceId = glGetUniformLocation(shaderProgramGrid, "uSurfaceId");
        textureGlobe.bind(shaderProgramGrid, "textureGlobe", 0);
        // Grid 1
        glUniform1f(locUTime, time);
        glUniform1i(locUSurfaceId, surfaceId);
        setModelMatrix(shaderProgramGrid, grid1);
        grid.getBuffers().draw(GL_TRIANGLES, shaderProgramGrid);
        // Grid 2
        textureBricks.bind(shaderProgramGrid, "textureBricks", 0);
        int surfaceId2 = 2;
        glUniform1i(locUSurfaceId, surfaceId2);
        setModelMatrix(shaderProgramGrid, grid2);
        gridstrip.getBuffers().draw(GL_TRIANGLE_STRIP, shaderProgramGrid);
    }
    private void drawSecond() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glUseProgram(shaderProgramQuad);
        renderTarget.bindColorTexture(shaderProgramQuad, "textureScene", 0);
        aoBlurTarget.bindColorTexture(shaderProgramQuad, "textureAO", 1);
        quad.getBuffers().draw(GL_TRIANGLES, shaderProgramQuad);
    }
    private void drawAO() {
        aoTarget.bind();
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(shaderProgramAO);

        renderTarget.bindDepthTexture(shaderProgramAO, "depthTex", 0);

        int locRes = glGetUniformLocation(shaderProgramAO, "uResolution");
        glUniform2f(locRes, width, height);

        int locRadius = glGetUniformLocation(shaderProgramAO, "uRadius");
        int locBias   = glGetUniformLocation(shaderProgramAO, "uBias");
        int locInt    = glGetUniformLocation(shaderProgramAO, "uIntensity");

        glUniform1f(locRadius, 3.0f);
        glUniform1f(locBias,   0.02f);
        glUniform1f(locInt,    1.0f);

        quad.getBuffers().draw(GL_TRIANGLES, shaderProgramAO);
    }
    private void drawAOBlur() {
        aoBlurTarget.bind();
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT);

        glUseProgram(shaderProgramAOBlur);

        aoTarget.bindColorTexture(shaderProgramAOBlur, "aoTex", 0);

        int locRes = glGetUniformLocation(shaderProgramAOBlur, "uResolution");
        glUniform2f(locRes, width, height);

        quad.getBuffers().draw(GL_TRIANGLES, shaderProgramAOBlur);
    }
    private void setGlobalUniforms(int shaderProgram) {
        int locUView = glGetUniformLocation(shaderProgram, "uView");
        int locUProj = glGetUniformLocation(shaderProgram, "uProj");

        glUniformMatrix4fv(locUView, false, camera.getViewMatrix().floatArray());
        glUniformMatrix4fv(locUProj, false, proj.floatArray());
    }
    private void setModelMatrix(int shaderProgram, Mat4 model) {
        int locUModel = glGetUniformLocation(shaderProgram, "uModel");
        if (locUModel >= 0) {
            glUniformMatrix4fv(locUModel, false, model.floatArray());
        }
    }
    private void setLightUniforms(int shaderProgram) {
        Vec3D eye = camera.getPosition();

        // složky barvy
        int locAmb = glGetUniformLocation(shaderProgramGrid, "uAmbientColor");
        int locDiff = glGetUniformLocation(shaderProgramGrid, "uDiffuseColor");
        int locSpec = glGetUniformLocation(shaderProgramGrid, "uSpecularColor");
        int locShine = glGetUniformLocation(shaderProgramGrid, "uShininess");
        glUniform3f(locAmb, 0.2f, 0.2f, 0.2f);
        glUniform3f(locDiff, 0.9f, 0.8f, 0.8f);
        glUniform3f(locSpec, 1.0f, 1.0f, 1.0f);
        glUniform1f(locShine, 32.0f);

        // pozice světla
        int locLightPos = glGetUniformLocation(shaderProgram, "uLightPos");
        glUniform3f(locLightPos, (float) lightPos.getX(), (float) lightPos.getY(), (float) lightPos.getZ());

        // normalizace
        int locLightDir = glGetUniformLocation(shaderProgram, "uLightDir");
        double lx = lightDir.getX();
        double ly = lightDir.getY();
        double lz = lightDir.getZ();
        double len = Math.sqrt(lx*lx + ly*ly + lz*lz);
        if (len == 0) len = 1;
        float ndx = (float) (lx / len);
        float ndy = (float) (ly / len);
        float ndz = (float) (lz / len);
        glUniform3f(locLightDir, ndx, ndy, ndz);

        //kamera
        int locViewPos  = glGetUniformLocation(shaderProgram, "uViewPos");
        glUniform3f(locViewPos, (float) eye.getX(), (float) eye.getY(), (float) eye.getZ());

        // reflektor
        float inner = (float) Math.cos(Math.toRadians(20));
        float outer = (float) Math.cos(Math.toRadians(40));
        int locInner = glGetUniformLocation(shaderProgramGrid, "uInnerCutOff");
        int locOuter = glGetUniformLocation(shaderProgramGrid, "uOuterCutOff");
        glUniform1f(locInner, inner);
        glUniform1f(locOuter, outer);

        // útlum světla
        int locCA = glGetUniformLocation(shaderProgramGrid, "uConstantAtt");
        int locLA = glGetUniformLocation(shaderProgramGrid, "uLinearAtt");
        int locQA = glGetUniformLocation(shaderProgramGrid, "uQuadraticAtt");
        glUniform1f(locCA, 1.0f);
        glUniform1f(locLA, 0.09f);
        glUniform1f(locQA, 0.032f);

        // přepínače složek
        int locEA = glGetUniformLocation(shaderProgram, "uEnableAmbient");
        int locED = glGetUniformLocation(shaderProgram, "uEnableDiffuse");
        int locES = glGetUniformLocation(shaderProgram, "uEnableSpecular");
        int locESp = glGetUniformLocation(shaderProgram, "uEnableSpot");
        int locEAtt = glGetUniformLocation(shaderProgram, "uEnableAttenuation");
        glUniform1i(locEA, enableAmbient ? 1 : 0);
        glUniform1i(locED, enableDiffuse ? 1 : 0);
        glUniform1i(locES, enableSpecular ? 1 : 0);
        glUniform1i(locESp, enableSpot ? 1 : 0);
        glUniform1i(locEAtt, enableAttenuation ? 1 : 0);
    }
    private void updateLightModelAndDir() {
        Vec3D lightTarget = new Vec3D(0, 0, 0);
        lightDir = lightTarget.sub(lightPos);
        lightModel = new Mat4Transl(
                lightPos.getX(),
                lightPos.getY(),
                lightPos.getZ()
        ).mul(new Mat4Scale(0.2f, 0.2f, 0.2f));
    }
    private void updateCameraMovement() {
        double s = 0.05;
        if (keyW) camera = camera.forward(s);
        if (keyS) camera = camera.backward(s);
        if (keyA) camera = camera.left(s);
        if (keyD) camera = camera.right(s);
        if (keyQ) camera = camera.up(s);
        if (keyE) camera = camera.down(s);
    }
    private void updateProjection() {
        if (height == 0) return;

        float aspect = width / (float) height;

        if (usePerspective) {
            proj = new Mat4PerspRH(fov, aspect, 0.1, 100.0);
        } else {
            double orthoSize = 6.0;
            double height = orthoSize * 2.0;
            double width = height * aspect;
            proj = new Mat4OrthoRH( width, height, 0.1, 100);
        }
    }

    private final GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true);
            }
            boolean pressed = (action == GLFW_PRESS || action == GLFW_REPEAT);
            double step = 0.05;
            switch (key) {
                case GLFW_KEY_W -> keyW = pressed;
                case GLFW_KEY_S -> keyS = pressed;
                case GLFW_KEY_A -> keyA = pressed;
                case GLFW_KEY_D -> keyD = pressed;
                case GLFW_KEY_Q -> keyQ = pressed;
                case GLFW_KEY_E -> keyE = pressed;
                case GLFW_KEY_I -> { // vpřed (zmenšit Z)
                    lightPos = new Vec3D(lightPos.getX(), lightPos.getY() - step, lightPos.getZ());
                    updateLightModelAndDir();
                }
                case GLFW_KEY_K -> { // vzad (zvětšit Z)
                    lightPos = new Vec3D(lightPos.getX(), lightPos.getY() + step, lightPos.getZ());
                    updateLightModelAndDir();
                }
                case GLFW_KEY_J -> { // doleva (zmenšit X)
                    lightPos = new Vec3D(lightPos.getX() + step, lightPos.getY(), lightPos.getZ());
                    updateLightModelAndDir();
                }
                case GLFW_KEY_L -> { // doprava (zvětšit X)
                    lightPos = new Vec3D(lightPos.getX() - step, lightPos.getY(), lightPos.getZ());
                    updateLightModelAndDir();
                }
                case GLFW_KEY_U -> { // dolů (zmenšit Y)
                    lightPos = new Vec3D(lightPos.getX(), lightPos.getY(), lightPos.getZ() + step);
                    updateLightModelAndDir();
                }
                case GLFW_KEY_O -> { // nahoru (zvětšit Y)
                    lightPos = new Vec3D(lightPos.getX(), lightPos.getY(), lightPos.getZ() - step);
                    updateLightModelAndDir();
                }
                case GLFW_KEY_Z -> modelScale *= 1.1;   // zvětšit
                case GLFW_KEY_X -> modelScale /= 1.1;   // zmenšit
                case GLFW_KEY_C -> modelRotationY += 0.1;
            }
            if (action == GLFW_PRESS){
                switch (key){
                    case GLFW_KEY_1 -> surfaceId = 0;
                    case GLFW_KEY_2 -> surfaceId = 1;
                    case GLFW_KEY_3 -> surfaceId = 2;
                    case GLFW_KEY_4 -> surfaceId = 3;
                    case GLFW_KEY_5 -> surfaceId = 4;
                    case GLFW_KEY_6 -> surfaceId = 5;
                    case GLFW_KEY_F1 -> enableAmbient = !enableAmbient;
                    case GLFW_KEY_F2 -> enableDiffuse = !enableDiffuse;
                    case GLFW_KEY_F3 -> enableSpecular = !enableSpecular;
                    case GLFW_KEY_F4 -> enableSpot = !enableSpot;
                    case GLFW_KEY_F5 -> enableAttenuation = !enableAttenuation;
                    case GLFW_KEY_P -> {
                        usePerspective = !usePerspective;
                        updateProjection();
                    }
                }
            }
            }
	};

    private final GLFWWindowSizeCallback wsCallback = new GLFWWindowSizeCallback() {
        @Override
        public void invoke(long window, int w, int h) {
            if (w > 0 && h > 0) {
                width = w;
                height = h;
                System.out.println("Renderer resize to [" + w + ", " + h + "]");

                // přepočítat projekci
                updateProjection();

                // přegenerovat renderTarget na novou velikost okna
                renderTarget = new OGLRenderTarget(width, height);
                aoTarget = new OGLRenderTarget(width, height);
                aoBlurTarget = new OGLRenderTarget(width, height);

                // případně nastavit viewport hned (není nutné, děláš to v drawFirst())
                glViewport(0, 0, width, height);
            }
        }
    };


    private final GLFWMouseButtonCallback mbCallback = new GLFWMouseButtonCallback () {
		@Override
		public void invoke(long window, int button, int action, int mods) {
            if (button == GLFW_MOUSE_BUTTON_RIGHT) {
                if (action == GLFW_PRESS) {
                    rightMouseDown = true;
                    firstMouse = true;
                } else if (action == GLFW_RELEASE) {
                    rightMouseDown = false;
                }
            }
		}
		
	};
	
    private final GLFWCursorPosCallback cpCallbacknew = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double xpos, double ypos) {
            if (!rightMouseDown) {
                return;
            }
            if (firstMouse) {
                lastMouseX = xpos;
                lastMouseY = ypos;
                firstMouse = false;
                return;
            }
            double dx = xpos - lastMouseX;
            double dy = ypos - lastMouseY;
            lastMouseX = xpos;
            lastMouseY = ypos;
            double mouseSensitivity = 0.005;
            camera = camera
                    .addAzimuth(-dx * mouseSensitivity)
                    .addZenith(-dy * mouseSensitivity);
    	}
    };
    
    private final GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
        @Override
        public void invoke (long window, double dx, double dy) {
            double speed = 0.1;
            if (dy > 0) {
                camera = camera.forward(speed);
            } else if (dy < 0) {
                camera = camera.backward(speed);
            }
        }
    };

    @Override
    public GLFWKeyCallback getKeyCallback() {
        return keyCallback;
    }
    @Override
    public GLFWWindowSizeCallback getWsCallback() {
        return wsCallback;
    }
    @Override
    public GLFWMouseButtonCallback getMouseCallback() {
        return mbCallback;
    }
    @Override
    public GLFWCursorPosCallback getCursorCallback() {
        return cpCallbacknew;
    }
    @Override
    public GLFWScrollCallback getScrollCallback() {
        return scrollCallback;
    }
}