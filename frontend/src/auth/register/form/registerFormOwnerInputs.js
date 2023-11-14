import { registerFormVetInputs } from "./registerFormVetInputs";
import { formValidators } from "../../../validators/formValidators";

export const registerFormOwnerInputs = [
  {
    tag: "Username",
    name: "username",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
  },
  {
    tag: "Password",
    name: "password",
    type: "password",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
  },
  {
    tag: "Name",
    name: "firstName",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
  },
];
