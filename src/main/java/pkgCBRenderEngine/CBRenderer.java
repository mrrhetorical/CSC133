package pkgCBRenderEngine;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import pkgCBUtils.CBWindowManager;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWaitEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public class CBRenderer {
	private int shader_program;
	private int NUM_COLS;
	private Matrix4f viewProjMatrix;
	private final int VPT = 4; // Vertices per tile
	private int[] winWidthHeight;
	private static final int OGL_MATRIX_SIZE = 1;
	private FloatBuffer myFloatBuffer;
	private int NUM_ROWS;
	private int PADDING;
	private final int EPT = 6; // Indices per tile? Going off of specification UML
	private static int SIZE;
	private CBWindowManager myWM;
	private int OFFSET;
	private final int FPV = 2; // Number of floats (coordinates) per vertex
	private int vpMatLocation;
	private int renderColorLocation;

	public static void main(String... args) {
		final int numRows = 6, numCols = 7, polyLength = 50, polyOffset = 10, polyPadding = 20;
		final int winWidth = (polyLength + polyPadding) * numCols + 2 * polyOffset;
		final int winHeight = (polyLength + polyPadding) * numRows + 2 * polyOffset;
		final int winOrgX = 50, winOrgY = 80;
		final CBWindowManager myWM = CBWindowManager.get(winWidth, winHeight, winOrgX, winOrgY);
		final CBRenderer myRenderer = new CBRenderer(myWM);
		myRenderer.PADDING = 20;
		myRenderer.OFFSET = 10;
		myRenderer.SIZE = 50;
		myRenderer.generateTilesVertices(6, 7);
	}

	public CBRenderer(CBWindowManager windowManager) {
		myWM = windowManager;

		winWidthHeight = myWM.getWindowSize();
	}

	private float[] generateTilesVertices(final int rowTiles, final int columnTiles) {
		float[] vertices = new float[rowTiles * columnTiles * VPT * FPV];
		for (int row = 0; row < rowTiles; row++) {
			for (int col = 0; col < columnTiles; col++) {
				int myIndx = (row * columnTiles + col) * VPT * FPV;
				float xmin = OFFSET + col * (SIZE * PADDING);
				float ymin = winWidthHeight[1] - (OFFSET + SIZE + row * (SIZE + PADDING));

				// First vertex
				vertices[myIndx] = xmin;
				vertices[myIndx + 1] = ymin;

				// Second vertex
				vertices[myIndx + 2] = xmin + SIZE;
				vertices[myIndx + 3] = ymin;

				// Third vertex
				vertices[myIndx + 4] = xmin + SIZE;
				vertices[myIndx + 5] = ymin - SIZE;

				// Fourth vertex
				vertices[myIndx + 6] = xmin;
				vertices[myIndx + 7] = ymin - SIZE;
			}
		}
		return vertices;
	}

	private int[] generateTileIndices(final int rows, final int cols) {
		int[] indices = new int[rows * cols * EPT];

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int tileNum = row * cols + col;
				int startIndex = tileNum * EPT;
				int startIV = tileNum * VPT;

				indices[startIndex] = startIV;
				indices[startIndex + 1] = startIV + 1;
				indices[startIndex + 2] = startIV + 2;
				indices[startIndex + 3] = startIV;
				indices[startIndex + 4] = startIV + 2;
				indices[startIndex + 5] = startIV + 3;
			}
		}

		return indices;
	}

	private void initOpenGL() {
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glViewport(0, 0, winWidthHeight[0], winWidthHeight[1]);
		glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
		this.shader_program = glCreateProgram();
		int vs = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vs,
				"uniform mat4 viewProjMatrix;" +
						"void main(void) {" +
						" gl_Position = viewProjMatrix * gl_Vertex;" +
						"}");
		glCompileShader(vs);
		glAttachShader(shader_program, vs);
		int fs = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fs,
				"uniform vec3 color;" +
						"void main(void) {" +
						" gl_FragColor = vec4(0.6f, 0.7f, 0.1f, 1.0f);" +
						"}");
		glCompileShader(fs);
		glAttachShader(shader_program, fs);
		glLinkProgram(shader_program);
		glUseProgram(shader_program);
		vpMatLocation = glGetUniformLocation(shader_program, "viewProjMatrix");
	}

	public void render(final int offset, final int padding, final int size, final int numRows, final int numCols) {

	}

	private void renderLoop() {
		glfwPollEvents();
		initOpenGL();
		renderObjects();
		/* Process window messages in the main thread */
		while (!myWM.isGlfwWindowClosed()) {
			glfwWaitEvents();
		}
	}

	private void renderObjects() {
		while (!myWM.isGlfwWindowClosed()) {
			glfwPollEvents();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			int vbo = glGenBuffers();
			int ibo = glGenBuffers();
			float[] vertices = {-20f, -20f, 20f, -20f, 20f, 20f, -20f, 20f};
			int[] indices = {0, 1, 2, 0, 2, 3};
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.
					createFloatBuffer(vertices.length).
					put(vertices).flip(), GL_STATIC_DRAW);
			glEnableClientState(GL_VERTEX_ARRAY);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, (IntBuffer) BufferUtils.
					createIntBuffer(indices.length).
					put(indices).flip(), GL_STATIC_DRAW);
			glVertexPointer(2, GL_FLOAT, 0, 0L);
			viewProjMatrix.setOrtho(-100, 100, -100, 100, 0, 10);
			glUniformMatrix4fv(vpMatLocation, false,
					viewProjMatrix.get(myFloatBuffer));
			glUniform3f(renderColorLocation, 1.0f, 0.498f, 0.153f);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			final int VTD = 6; // need to process 6 Vertices To Draw 2 triangles
			glDrawElements(GL_TRIANGLES, VTD, GL_UNSIGNED_INT, 0L);
			myWM.swapBuffers();
		}
	}


}
