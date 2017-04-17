package com.compass.compass.classes.math;

/**
 * ****************************************************************************
 * Copyright 2011 See AUTHORS file.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ****************************************************************************
 */

import java.io.Serializable;

/**
 * A 3x3 <a href="http://en.wikipedia.org/wiki/Row-major_order">column major</a>
 * matrix; useful for 2D transforms.
 *
 * @author mzechner
 */
public class Matrix3 implements Serializable {
    private static final long serialVersionUID = 7907569533774959788L;
    private static final float DEGREE_TO_RAD = (float) Math.PI / 180;
    private static final int M00 = 0;
    private static final int M01 = 3;
    private static final int M02 = 6;
    private static final int M10 = 1;
    private static final int M11 = 4;
    private static final int M12 = 7;
    private static final int M20 = 2;
    private static final int M21 = 5;
    private static final int M22 = 8;
    public float[] val = new float[9];
    private float[] tmp = new float[9];

    public Matrix3() {
        idt();
    }

    public Matrix3(Matrix3 matrix) {
        set(matrix);
    }

    /**
     * Multiplies matrix a with matrix b in the following manner:
     * <p/>
     * <pre>
     * mul(A, B) => A := AB
     * </pre>
     *
     * @param mata The float array representing the first matrix. Must have at
     *             least 9 elements.
     * @param matb The float array representing the second matrix. Must have at
     *             least 9 elements.
     */
    private static void mul(float[] mata, float[] matb) {
        float v00 = (mata[M00] * matb[M00]) + (mata[M01] * matb[M10]) + (mata[M02] * matb[M20]);
        float v01 = (mata[M00] * matb[M01]) + (mata[M01] * matb[M11]) + (mata[M02] * matb[M21]);
        float v02 = (mata[M00] * matb[M02]) + (mata[M01] * matb[M12]) + (mata[M02] * matb[M22]);

        float v10 = (mata[M10] * matb[M00]) + (mata[M11] * matb[M10]) + (mata[M12] * matb[M20]);
        float v11 = (mata[M10] * matb[M01]) + (mata[M11] * matb[M11]) + (mata[M12] * matb[M21]);
        float v12 = (mata[M10] * matb[M02]) + (mata[M11] * matb[M12]) + (mata[M12] * matb[M22]);

        float v20 = (mata[M20] * matb[M00]) + (mata[M21] * matb[M10]) + (mata[M22] * matb[M20]);
        float v21 = (mata[M20] * matb[M01]) + (mata[M21] * matb[M11]) + (mata[M22] * matb[M21]);
        float v22 = (mata[M20] * matb[M02]) + (mata[M21] * matb[M12]) + (mata[M22] * matb[M22]);

        mata[M00] = v00;
        mata[M10] = v10;
        mata[M20] = v20;
        mata[M01] = v01;
        mata[M11] = v11;
        mata[M21] = v21;
        mata[M02] = v02;
        mata[M12] = v12;
        mata[M22] = v22;
    }

    /**
     * Sets this matrix to the identity matrix
     *
     * @return This matrix for the purpose of chaining operations.
     */
    Matrix3 idt() {
        val[M00] = 1;
        val[M10] = 0;
        val[M20] = 0;
        val[M01] = 0;
        val[M11] = 1;
        val[M21] = 0;
        val[M02] = 0;
        val[M12] = 0;
        val[M22] = 1;
        return this;
    }

    /**
     * Multiplies this matrix with the provided matrix and stores the result in
     * this matrix. For example:
     * <p/>
     * <pre>
     * A.mul(B) results in A := AB
     * </pre>
     *
     * @param m Matrix to multiply by.
     * @return This matrix for the purpose of chaining operations together.
     */
    public Matrix3 mul(Matrix3 m) {
        float v00 = (val[M00] * m.val[M00]) + (val[M01] * m.val[M10]) + (val[M02] * m.val[M20]);
        float v01 = (val[M00] * m.val[M01]) + (val[M01] * m.val[M11]) + (val[M02] * m.val[M21]);
        float v02 = (val[M00] * m.val[M02]) + (val[M01] * m.val[M12]) + (val[M02] * m.val[M22]);

        float v10 = (val[M10] * m.val[M00]) + (val[M11] * m.val[M10]) + (val[M12] * m.val[M20]);
        float v11 = (val[M10] * m.val[M01]) + (val[M11] * m.val[M11]) + (val[M12] * m.val[M21]);
        float v12 = (val[M10] * m.val[M02]) + (val[M11] * m.val[M12]) + (val[M12] * m.val[M22]);

        float v20 = (val[M20] * m.val[M00]) + (val[M21] * m.val[M10]) + (val[M22] * m.val[M20]);
        float v21 = (val[M20] * m.val[M01]) + (val[M21] * m.val[M11]) + (val[M22] * m.val[M21]);
        float v22 = (val[M20] * m.val[M02]) + (val[M21] * m.val[M12]) + (val[M22] * m.val[M22]);

        val[M00] = v00;
        val[M10] = v10;
        val[M20] = v20;
        val[M01] = v01;
        val[M11] = v11;
        val[M21] = v21;
        val[M02] = v02;
        val[M12] = v12;
        val[M22] = v22;

        return this;
    }

    /**
     * Sets this matrix to a rotation matrix that will rotate any vector in
     * counter-clockwise order around the z-axis.
     *
     * @param degrees the angle in degrees.
     * @return This matrix for the purpose of chaining operations.
     */
    public Matrix3 setToRotation(float degrees) {
        float angle = DEGREE_TO_RAD * degrees;
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        val[M00] = cos;
        val[M10] = sin;
        val[M20] = 0;

        val[M01] = -sin;
        val[M11] = cos;
        val[M21] = 0;

        val[M02] = 0;
        val[M12] = 0;
        val[M22] = 1;

        return this;
    }

    /**
     * Sets this matrix to a translation matrix.
     *
     * @param x the translation in x
     * @param y the translation in y
     * @return This matrix for the purpose of chaining operations.
     */
    public Matrix3 setToTranslation(float x, float y) {
        val[M00] = 1;
        val[M10] = 0;
        val[M20] = 0;

        val[M01] = 0;
        val[M11] = 1;
        val[M21] = 0;

        val[M02] = x;
        val[M12] = y;
        val[M22] = 1;

        return this;
    }

    /**
     * Sets this matrix to a translation matrix.
     *
     * @param translation The translation vector.
     * @return This matrix for the purpose of chaining operations.
     */
    public Matrix3 setToTranslation(Vector2 translation) {
        val[M00] = 1;
        val[M10] = 0;
        val[M20] = 0;

        val[M01] = 0;
        val[M11] = 1;
        val[M21] = 0;

        val[M02] = translation.x;
        val[M12] = translation.y;
        val[M22] = 1;

        return this;
    }

    /**
     * Sets this matrix to a scaling matrix.
     *
     * @param scaleX the scale in x
     * @param scaleY the scale in y
     * @return This matrix for the purpose of chaining operations.
     */
    public Matrix3 setToScaling(float scaleX, float scaleY) {
        val[M00] = scaleX;
        val[M10] = 0;
        val[M20] = 0;
        val[M01] = 0;
        val[M11] = scaleY;
        val[M21] = 0;
        val[M02] = 0;
        val[M12] = 0;
        val[M22] = 1;
        return this;
    }

    @Override
    public String toString() {
        return "[" + val[0] + "|" + val[3] + "|" + val[6] + "]\n" + "[" + val[1] + "|" + val[4] + "|" + val[7] + "]\n" + "[" + val[2] + "|" + val[5] + "|" + val[8] + "]";
    }

    float det() {
        return ((val[M00] * val[M11] * val[M22]) + (val[M01] * val[M12] * val[M20]) + (val[M02] * val[M10] * val[M21])) - (val[M00] * val[M12] * val[M21]) - (val[M01] * val[M10] * val[M22]) - (val[M02] * val[M11] * val[M20]);
    }

    /**
     * Inverts this matrix given that the determinant is != 0.
     *
     * @return This matrix for the purpose of chaining operations.
     */
    public Matrix3 inv() {
        float det = det();
        if (det == 0) {
            throw new Error("Can't invert a singular matrix"); // changed to
            // error from
            // GdxRuntimeException
        }

        float inv_det = 1.0f / det;

        tmp[M00] = (val[M11] * val[M22]) - (val[M21] * val[M12]);
        tmp[M10] = (val[M20] * val[M12]) - (val[M10] * val[M22]);
        tmp[M20] = (val[M10] * val[M21]) - (val[M20] * val[M11]);
        tmp[M01] = (val[M21] * val[M02]) - (val[M01] * val[M22]);
        tmp[M11] = (val[M00] * val[M22]) - (val[M20] * val[M02]);
        tmp[M21] = (val[M20] * val[M01]) - (val[M00] * val[M21]);
        tmp[M02] = (val[M01] * val[M12]) - (val[M11] * val[M02]);
        tmp[M12] = (val[M10] * val[M02]) - (val[M00] * val[M12]);
        tmp[M22] = (val[M00] * val[M11]) - (val[M10] * val[M01]);

        val[M00] = inv_det * tmp[M00];
        val[M10] = inv_det * tmp[M10];
        val[M20] = inv_det * tmp[M20];
        val[M01] = inv_det * tmp[M01];
        val[M11] = inv_det * tmp[M11];
        val[M21] = inv_det * tmp[M21];
        val[M02] = inv_det * tmp[M02];
        val[M12] = inv_det * tmp[M12];
        val[M22] = inv_det * tmp[M22];

        return this;
    }

    /**
     * Copies the values from the provided matrix to this matrix.
     *
     * @param mat The matrix to copy.
     * @return This matrix for the purposes of chaining.
     */
    Matrix3 set(Matrix3 mat) {
        System.arraycopy(mat.val, 0, val, 0, val.length);
        return this;
    }

    /**
     * Sets this 3x3 matrix to the top left 3x3 corner of the provided 4x4
     * matrix.
     *
     * @param mat The matrix whose top left corner will be copied. This matrix
     *            will not be modified.
     * @return This matrix for the purpose of chaining operations.
     */
    public Matrix3 set(Matrix4 mat) {
        val[M00] = mat.val[Matrix4.M00];
        val[M10] = mat.val[Matrix4.M10];
        val[M20] = mat.val[Matrix4.M20];
        val[M01] = mat.val[Matrix4.M01];
        val[M11] = mat.val[Matrix4.M11];
        val[M21] = mat.val[Matrix4.M21];
        val[M02] = mat.val[Matrix4.M02];
        val[M12] = mat.val[Matrix4.M12];
        val[M22] = mat.val[Matrix4.M22];
        return this;
    }

    /**
     * Adds a translational component to the matrix in the 3rd column. The other
     * columns are untouched.
     *
     * @param vector The translation vector.
     * @return This matrix for the purpose of chaining.
     */
    public Matrix3 trn(Vector2 vector) {
        val[M02] += vector.x;
        val[M12] += vector.y;
        return this;
    }

    /**
     * Adds a translational component to the matrix in the 3rd column. The other
     * columns are untouched.
     *
     * @param x The x-component of the translation vector.
     * @param y The y-component of the translation vector.
     * @return This matrix for the purpose of chaining.
     */
    public Matrix3 trn(float x, float y) {
        val[M02] += x;
        val[M12] += y;
        return this;
    }

    /**
     * Adds a translational component to the matrix in the 3rd column. The other
     * columns are untouched.
     *
     * @param vector The translation vector. (The z-component of the vector is
     *               ignored because this is a 3x3 matrix)
     * @return This matrix for the purpose of chaining.
     */
    public Matrix3 trn(Vector3 vector) {
        val[M02] += vector.x;
        val[M12] += vector.y;
        return this;
    }

    /**
     * Postmultiplies this matrix by a translation matrix. Postmultiplication is
     * also used by OpenGL ES' 1.x glTranslate/glRotate/glScale.
     *
     * @param x The x-component of the translation vector.
     * @param y The y-component of the translation vector.
     * @return This matrix for the purpose of chaining.
     */
    public Matrix3 translate(float x, float y) {
        tmp[M00] = 1;
        tmp[M10] = 0;
        tmp[M20] = 0;

        tmp[M01] = 0;
        tmp[M11] = 1;
        tmp[M21] = 0;

        tmp[M02] = x;
        tmp[M12] = y;
        tmp[M22] = 1;
        mul(val, tmp);
        return this;
    }

    /**
     * Postmultiplies this matrix by a translation matrix. Postmultiplication is
     * also used by OpenGL ES' 1.x glTranslate/glRotate/glScale.
     *
     * @param translation The translation vector.
     * @return This matrix for the purpose of chaining.
     */
    public Matrix3 translate(Vector2 translation) {
        tmp[M00] = 1;
        tmp[M10] = 0;
        tmp[M20] = 0;

        tmp[M01] = 0;
        tmp[M11] = 1;
        tmp[M21] = 0;

        tmp[M02] = translation.x;
        tmp[M12] = translation.y;
        tmp[M22] = 1;
        mul(val, tmp);
        return this;
    }

    /**
     * Postmultiplies this matrix with a (counter-clockwise) rotation matrix.
     * Postmultiplication is also used by OpenGL ES' 1.x
     * glTranslate/glRotate/glScale.
     *
     * @param angle The angle in degrees
     * @return This matrix for the purpose of chaining.
     */
    public Matrix3 rotate(float angle) {
        if (angle == 0) {
            return this;
        }
        angle = DEGREE_TO_RAD * angle;
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        tmp[M00] = cos;
        tmp[M10] = sin;
        tmp[M20] = 0;

        tmp[M01] = -sin;
        tmp[M11] = cos;
        tmp[M21] = 0;

        tmp[M02] = 0;
        tmp[M12] = 0;
        tmp[M22] = 1;
        mul(val, tmp);
        return this;
    }

    /**
     * Postmultiplies this matrix with a scale matrix. Postmultiplication is
     * also used by OpenGL ES' 1.x glTranslate/glRotate/glScale.
     *
     * @param scaleX The scale in the x-axis.
     * @param scaleY The scale in the y-axis.
     * @return This matrix for the purpose of chaining.
     */
    public Matrix3 scale(float scaleX, float scaleY) {
        tmp[M00] = scaleX;
        tmp[M10] = 0;
        tmp[M20] = 0;
        tmp[M01] = 0;
        tmp[M11] = scaleY;
        tmp[M21] = 0;
        tmp[M02] = 0;
        tmp[M12] = 0;
        tmp[M22] = 1;
        mul(val, tmp);
        return this;
    }

    /**
     * Postmultiplies this matrix with a scale matrix. Postmultiplication is
     * also used by OpenGL ES' 1.x glTranslate/glRotate/glScale.
     *
     * @param scale The vector to scale the matrix by.
     * @return This matrix for the purpose of chaining.
     */
    public Matrix3 scale(Vector2 scale) {
        tmp[M00] = scale.x;
        tmp[M10] = 0;
        tmp[M20] = 0;
        tmp[M01] = 0;
        tmp[M11] = scale.y;
        tmp[M21] = 0;
        tmp[M02] = 0;
        tmp[M12] = 0;
        tmp[M22] = 1;
        mul(val, tmp);
        return this;
    }

    /**
     * Get the values in this matrix.
     *
     * @return The float values that make up this matrix in column-major order.
     */
    public float[] getValues() {
        return val;
    }

    /**
     * Scale the matrix in the both the x and y components by the scalar value.
     *
     * @param scale The single value that will be used to scale both the x and y
     *              components.
     * @return This matrix for the purpose of chaining methods together.
     */
    public Matrix3 scl(float scale) {
        val[M00] *= scale;
        val[M11] *= scale;
        return this;
    }

    /**
     * Scale this matrix using the x and y components of the vector but leave
     * the rest of the matrix alone.
     *
     * @param scale The {@link Vector3} to use to scale this matrix.
     * @return This matrix for the purpose of chaining methods together.
     */
    public Matrix3 scl(Vector2 scale) {
        val[M00] *= scale.x;
        val[M11] *= scale.y;
        return this;
    }

    /**
     * Scale this matrix using the x and y components of the vector but leave
     * the rest of the matrix alone.
     *
     * @param scale The {@link Vector3} to use to scale this matrix. The z
     *              component will be ignored.
     * @return This matrix for the purpose of chaining methods together.
     */
    public Matrix3 scl(Vector3 scale) {
        val[M00] *= scale.x;
        val[M11] *= scale.y;
        return this;
    }

    /**
     * Transposes the current matrix.
     *
     * @return This matrix for the purpose of chaining methods together.
     */
    public Matrix3 transpose() {
        // Where MXY you do not have to change MXX
        float v01 = val[M10];
        float v02 = val[M20];
        float v10 = val[M01];
        float v12 = val[M21];
        float v20 = val[M02];
        float v21 = val[M12];
        val[M01] = v01;
        val[M02] = v02;
        val[M10] = v10;
        val[M12] = v12;
        val[M20] = v20;
        val[M21] = v21;
        return this;
    }
}