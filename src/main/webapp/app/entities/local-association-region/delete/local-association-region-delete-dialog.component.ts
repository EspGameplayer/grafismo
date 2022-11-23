import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocalAssociationRegion } from '../local-association-region.model';
import { LocalAssociationRegionService } from '../service/local-association-region.service';

@Component({
  templateUrl: './local-association-region-delete-dialog.component.html',
})
export class LocalAssociationRegionDeleteDialogComponent {
  localAssociationRegion?: ILocalAssociationRegion;

  constructor(protected localAssociationRegionService: LocalAssociationRegionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.localAssociationRegionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
