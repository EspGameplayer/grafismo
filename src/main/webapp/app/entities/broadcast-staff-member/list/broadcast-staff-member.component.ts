import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBroadcastStaffMember } from '../broadcast-staff-member.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { BroadcastStaffMemberService } from '../service/broadcast-staff-member.service';
import { BroadcastStaffMemberDeleteDialogComponent } from '../delete/broadcast-staff-member-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-broadcast-staff-member',
  templateUrl: './broadcast-staff-member.component.html',
})
export class BroadcastStaffMemberComponent implements OnInit {
  broadcastStaffMembers: IBroadcastStaffMember[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected broadcastStaffMemberService: BroadcastStaffMemberService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.broadcastStaffMembers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.broadcastStaffMemberService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IBroadcastStaffMember[]>) => {
          this.isLoading = false;
          this.paginateBroadcastStaffMembers(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.broadcastStaffMembers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBroadcastStaffMember): number {
    return item.id!;
  }

  delete(broadcastStaffMember: IBroadcastStaffMember): void {
    const modalRef = this.modalService.open(BroadcastStaffMemberDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.broadcastStaffMember = broadcastStaffMember;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateBroadcastStaffMembers(data: IBroadcastStaffMember[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.broadcastStaffMembers.push(d);
      }
    }
  }
}
