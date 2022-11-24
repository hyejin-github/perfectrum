// import React from "react";
import styled from "styled-components";

export const Select = styled.select`
  margin: 0;
  min-width: 0;
  display: block;
  width: 100%;
  padding: 8px 8px;
  font-size: fs-15;
  line-height: inherit;
  border: 1px solid;
  border-radius: 4px;
  color: $GRAY-DB;
  background-color: $GRAY-DB;
  &:focus {
    border-color: $BLACK;
  }
`;
export default Select;
// function selectItem(props) {
//   return (
//     <Select>
//       {props.options.map(option => (
//         <option key={option.value} value={option.value}>
//           {option.name}
//         </option>
//       ))}
//     </Select>
//   );
// }
// export default selectItem;
