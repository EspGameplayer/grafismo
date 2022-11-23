import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBroadcastStaffMember, BroadcastStaffMember } from '../broadcast-staff-member.model';
import { BroadcastStaffMemberService } from '../service/broadcast-staff-member.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'jhi-broadcast-staff-member-update',
  templateUrl: './broadcast-staff-member-update.component.html',
})
export class BroadcastStaffMemberUpdateComponent implements OnInit {
  isSaving = false;

  peopleCollection: IPerson[] = [];

  editForm = this.fb.group({
    id: [],
    person: [null, Validators.required],
  });

  constructor(
    protected broadcastStaffMemberService: BroadcastStaffMemberService,
    protected personService: PersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ broadcastStaffMember }) => {
      this.updateForm(broadcastStaffMember);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const broadcastStaffMember = this.createFromForm();
    if (broadcastStaffMember.id !== undefined) {
      this.subscribeToSaveResponse(this.broadcastStaffMemberService.update(broadcastStaffMember));
    } else {
      this.subscribeToSaveResponse(this.broadcastStaffMemberService.create(broadcastStaffMember));
    }
  }

  trackPersonById(_index: number, item: IPerson): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBroadcastStaffMember>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(broadcastStaffMember: IBroadcastStaffMember): void {
    this.editForm.patchValue({
      id: broadcastStaffMember.id,
      person: broadcastStaffMember.person,
    });

    this.peopleCollection = this.personService.addPersonToCollectionIfMissing(this.peopleCollection, broadcastStaffMember.person);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query({ filter: 'broadcaststaffmember-is-null' })
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleCollection = people));
  }

  protected createFromForm(): IBroadcastStaffMember {
    return {
      ...new BroadcastStaffMember(),
      id: this.editForm.get(['id'])!.value,
      person: this.editForm.get(['person'])!.value,
    };
  }
}
