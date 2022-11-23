import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssociation } from '../association.model';
import { AssociationService } from '../service/association.service';

@Component({
  templateUrl: './association-delete-dialog.component.html',
})
export class AssociationDeleteDialogComponent {
  association?: IAssociation;

  constructor(protected associationService: AssociationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.associationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
