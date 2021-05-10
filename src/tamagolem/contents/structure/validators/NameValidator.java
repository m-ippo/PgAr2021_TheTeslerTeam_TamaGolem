/*
 * Copyright 2021 TTT.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tamagolem.contents.structure.validators;

import ttt.utils.console.input.interfaces.Validator;

/**
 * Serve per validare i nomi inseriti in input.
 *
 * @author TTT
 */
public class NameValidator implements Validator<String> {

    public NameValidator() {
    }

    @Override
    public void validate(String value) throws IllegalArgumentException {
        if (value == null || value.isEmpty() || "".equals(value.trim())) {
            throw new IllegalArgumentException("Il nome non è valido");
        }
    }

}
